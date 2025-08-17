package cl.bci.user.application.port;

import java.util.List;
import java.util.Optional;

import cl.bci.user.domain.User;

public interface UserRespositoryPort {
	User save(User user);
	void delete(Long id);
	Optional<User> findById(Long id);
	List<User> findAll();
	boolean existsById(Long id);
	boolean existsByEmail(String email);
}
