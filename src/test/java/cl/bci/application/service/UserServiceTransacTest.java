package cl.bci.application.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cl.bci.common.exception.EmailException;
import cl.bci.user.application.service.UserService;
import cl.bci.user.infrastructure.rest.dto.PhoneRequest;
import cl.bci.user.infrastructure.rest.dto.UserRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTransacTest {
	@Autowired
	private UserService userService;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Test
	void createUser_rollbackTransaction_duplicateEmail() {
		// Usuario OK
		UserRequest request1 = new UserRequest();
		request1.setEmail("test@example.com");
		request1.setName("Test Uno");
		request1.setPassword("Password123");
		request1.setPhones(List.of(new PhoneRequest("12345678", "1", "56")));
		assertDoesNotThrow(() -> userService.createUser(request1)); // Este usuario se crea

		// Usuario con error, mail duplicado
		UserRequest request2 = new UserRequest();
		request2.setEmail("test@example.com");
		request2.setName("Test Dos");
		request2.setPassword("Password123");
		request2.setPhones(List.of(new PhoneRequest("87654321", "2", "56")));
		
		// Verificamos que se lanza una excepción al intentar crear el segundo usuario(duplicado)
		assertThrows(EmailException.class, () -> userService.createUser(request2));
	}
}