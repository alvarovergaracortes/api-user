package cl.bci.user.infrastructure.persistence.mapper;

import java.util.stream.Collectors;

import cl.bci.user.domain.Phone;
import cl.bci.user.domain.User;
import cl.bci.user.infrastructure.persistence.entity.PhoneEntity;
import cl.bci.user.infrastructure.persistence.entity.UserEntity;

public class UserEntityMapper {
	
	public User toDomain(UserEntity entity) {
		return new User(
				entity.getId(),
				entity.getName(),
				entity.getEmail(),
				entity.getPassword(),
				entity.getPhones().stream().map(this::toDomain).collect(Collectors.toList()),
				entity.getCreated(),
				entity.getModified(),
				entity.getLastLogin(),
				entity.getToken(),
				entity.isActive(),
				entity.getRoles()
			);
	}
	
	private Phone toDomain(PhoneEntity phoneEntity) {
		return new Phone(
				phoneEntity.getId(),
				phoneEntity.getNumber(),
				phoneEntity.getCitycode(),
				phoneEntity.getCountrycode()
		);
	}
	
	public UserEntity toEntity(User user) {
		if (user == null) {
			return null;
		}
		
		UserEntity userEntity = new UserEntity();
		userEntity.setId(user.getId());
		userEntity.setName(user.getName());
		userEntity.setEmail(user.getEmail());
		userEntity.setPassword(user.getPassword());
		userEntity.setCreated(user.getCreated());
		userEntity.setModified(user.getModified());
		userEntity.setLastLogin(user.getLastLogin());
		userEntity.setToken(user.getToken());
		userEntity.setActive(user.isActive());
		
		userEntity.setPhones(
				user.getPhones().stream()
				.map(phone -> toEntity(phone, userEntity))
				.collect(Collectors.toList())
		);
		
		return userEntity;
	}
	
	private PhoneEntity toEntity(Phone phone, UserEntity userEntity) {
		return new PhoneEntity(
				phone.getId(),
				phone.getNumber(),
				phone.getCitycode(),
				phone.getContrycode(),
				userEntity
			);
	}
}
