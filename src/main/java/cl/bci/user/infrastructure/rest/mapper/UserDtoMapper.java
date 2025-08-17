package cl.bci.user.infrastructure.rest.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import cl.bci.user.infrastructure.persistence.entity.PhoneEntity;
import cl.bci.user.infrastructure.persistence.entity.UserEntity;
import cl.bci.user.infrastructure.rest.dto.PhoneResponse;
import cl.bci.user.infrastructure.rest.dto.UserRequest;
import cl.bci.user.infrastructure.rest.dto.UserResponse;

@Component
public class UserDtoMapper {
	
	public UserResponse toResponse(UserEntity user) {
		List<PhoneResponse> phones = user.getPhones().stream()
				.map(p -> new PhoneResponse(p.getNumber(), p.getCitycode(), p.getCountrycode()))
				.toList();
		
		return new UserResponse(
				user.getId(),
				user.getName(),
				user.getEmail(),
				user.getCreated(),
				user.getModified(),
				user.getLastLogin(),
				user.getToken(),
				user.isActive(),
				phones
			);	
	}
	
	
	public UserResponse toResponseSucces(UserEntity user) {
		return new UserResponse(
				user.getId(),
				null,
				null,
				user.getCreated(),
				user.getModified(),
				user.getLastLogin(),
				user.getToken(),
				user.isActive(),
				null
			);
		
	}
	
	public void mapUpdate(UserEntity entity, UserRequest request) {
		entity.setName(request.getName());
		entity.setPassword(request.getPassword());
		entity.setModified(LocalDateTime.now());
		if (request.getRoles()!=null || !"".equals(request.getRoles())) {
			entity.setRoles(request.getRoles().toUpperCase());
		}
		
		if (request.getActive()!=null) {
			entity.setActive(request.getActive());
		}

		
		List<PhoneEntity> phones = request.getPhones().stream()
				.map(p -> new PhoneEntity(null, p.getNumber(), p.getCitycode(), p.getContrycode(), entity))
				.toList();
		
		entity.getPhones().clear();
		entity.getPhones().addAll(phones);
	}
}
