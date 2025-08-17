package cl.bci.user.infrastructure.persistence.mapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import cl.bci.user.domain.User;
import cl.bci.user.infrastructure.persistence.entity.PhoneEntity;
import cl.bci.user.infrastructure.persistence.entity.UserEntity;
import cl.bci.user.infrastructure.rest.dto.LoginRequest;
import cl.bci.user.infrastructure.rest.dto.UserRequest;

@Component
public class UserMapper {
	public UserEntity toEntity(UserRequest request, UUID id, String token, LocalDateTime now) {
		UserEntity user = new UserEntity();
		user.setId(id);
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());
		user.setCreated(now);
		user.setModified(now);
		user.setLastLogin(now);
		user.setActive(true);
		user.setToken(token);
		user.setRoles(request.getRoles().toUpperCase());
		
		List<PhoneEntity> phones = Optional.ofNullable(request.getPhones())
				.orElse(Collections.emptyList())
				.stream()
				.map(p -> new PhoneEntity(null, p.getNumber(), p.getCitycode(), p.getContrycode(), user))
				.toList();
		
		user.setPhones(phones);
		return user;
	}
	
	public User toUser(LoginRequest login) {
		User user = new User();
		user.setEmail(login.getEmail());
		user.setPassword(login.getPassword());
		
		return user;
	}
}