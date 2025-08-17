package cl.bci.user.application.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cl.bci.common.dto.ErrorCode;
import cl.bci.common.exception.CredencialesInvalidasException;
import cl.bci.common.exception.UserNotFoundException;
import cl.bci.common.security.JwtUtil;
import cl.bci.common.security.PasswordUtil;
import cl.bci.user.domain.User;
import cl.bci.user.infrastructure.persistence.UserJpaRepository;
import cl.bci.user.infrastructure.persistence.entity.UserEntity;

@Service
public class LoginService implements LoginServicePort{
	private final UserJpaRepository userJpaRepository;
	private final PasswordUtil passwordUtil;
	private final JwtUtil jwtUtil;
	

	public LoginService(UserJpaRepository userJpaRepository, PasswordUtil passwordUtil, JwtUtil jwtUtil) {
		this.userJpaRepository = userJpaRepository;
		this.passwordUtil = passwordUtil;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public Optional<String> login(User user) {
		
		System.out.println("user.toString(): " + user.toString());
		UserEntity userEntity = this.userJpaRepository.findByEmail(user.getEmail())
				.orElseThrow(()-> new UserNotFoundException("Usuario no encontrado"));
		
		System.out.println("user.getPassword(): " + user.getPassword() + "  -  userEntity.getPassword(): " + userEntity.getPassword());
		System.out.println("verifica password:  " + passwordUtil.matches(user.getPassword(), userEntity.getPassword()));
		
		if (!passwordUtil.matches(user.getPassword(), userEntity.getPassword())) {
			throw new CredencialesInvalidasException("Usuario o contraseña incorrecta", ErrorCode.INCORRECT_CREDENTIALS);
		}
		
		List<String> roles = Arrays.stream(userEntity.getRoles().split(","))
		        .map(String::trim)
		        .filter(s -> !s.isEmpty())
		        .toList();
		
		return Optional.of(jwtUtil.generateToken(userEntity.getEmail(), roles));
	}
}
