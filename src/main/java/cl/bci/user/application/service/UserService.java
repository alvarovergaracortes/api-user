package cl.bci.user.application.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.bci.common.exception.EmailException;
import cl.bci.common.exception.UserNotFoundException;
import cl.bci.common.security.JwtUtil;
import cl.bci.common.security.PasswordUtil;
import cl.bci.user.infrastructure.persistence.UserJpaRepository;
import cl.bci.user.infrastructure.persistence.entity.UserEntity;
import cl.bci.user.infrastructure.persistence.mapper.UserMapper;
import cl.bci.user.infrastructure.rest.dto.UserRequest;
import cl.bci.user.infrastructure.rest.dto.UserResponse;
import cl.bci.user.infrastructure.rest.mapper.UserDtoMapper;

@Service
public class UserService implements UserServicePort{
	private final UserJpaRepository userRepo;
	private final JwtUtil jwtService;
	private final UserMapper userMapper;
	private final UserDtoMapper userDtoMapper;
	private final PasswordUtil passwordUtil;

	


	public UserService(UserJpaRepository userRepo, JwtUtil jwtService, UserMapper userMapper,
			UserDtoMapper userDtoMapper, PasswordUtil passwordUtil) {
		this.userRepo = userRepo;
		this.jwtService = jwtService;
		this.userMapper = userMapper;
		this.userDtoMapper = userDtoMapper;
		this.passwordUtil = passwordUtil;
	}


	@Override
	@Transactional
	public UserResponse createUser(UserRequest request) {
		this.userRepo.findByEmail(request.getEmail()).ifPresent(u -> {
			throw new EmailException("El correo " + request.getEmail() + ", ya está registrado ");
		});
		/**
		 * Todo usuario que se crea tendra el ROL de Usuario
		 * Se raliza esto para mantener la esturctura del registro que viene sin rol
		 * 
		 */
		request.setRoles("USER");
		List<String> list = Arrays.asList(request.getRoles());
		
		String token = this.jwtService.generateToken(request.getEmail(), list);
		// encriptamos la password antes de almacenarla en BD
		String passEncripted = this.passwordUtil.encode(request.getPassword());
		request.setPassword(passEncripted);
		
		UserEntity userEntity = userMapper.toEntity(request, UUID.randomUUID(), token, LocalDateTime.now());
		UserEntity entity =  userRepo.save(userEntity);
		return userDtoMapper.toResponseSucces(entity);
	}


	@Override
	public UserResponse update(UUID id, UserRequest request) {
		UserEntity existing = userRepo.findById(id)
				.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
		
		userDtoMapper.mapUpdate(existing, request);
		
		UserEntity entity = userRepo.save(existing);
		
		return userDtoMapper.toResponseSucces(entity);
	}
	
	

	@Override
	public void delete(UUID id) {
		UserEntity user = userRepo.findById(id)
				.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
		
		userRepo.delete(user);
	}


	@Override
	public UserResponse findById(UUID id) {
		UserEntity entity = userRepo.findById(id)
				.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado: " + id));
		
		return userDtoMapper.toResponse(entity);
	}

	
	@Override
	public List<UserResponse> findAll() {
		return userRepo.findAll().stream()
				.map(userDtoMapper::toResponse)
				.collect(Collectors.toList());
	}
}
