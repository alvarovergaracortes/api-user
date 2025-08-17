package cl.bci.infrastructure.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import cl.bci.common.security.CustomAccessDeniedHandler;
import cl.bci.common.security.CustomAuthenticationEntryPoint;
import cl.bci.common.security.JwtUtil;
import cl.bci.common.security.SecurityConfig;
import cl.bci.user.application.service.UserServicePort;
import cl.bci.user.infrastructure.rest.UserController;
import cl.bci.user.infrastructure.rest.dto.UserRequest;
import cl.bci.user.infrastructure.rest.dto.UserResponse;


@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
@Import({SecurityConfig.class, CustomAccessDeniedHandler.class, CustomAuthenticationEntryPoint.class})
public class UserControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@MockitoBean
	private UserServicePort service;
	
	@MockitoBean
	private JwtUtil jwtUtil;

	private final String email = "juan@gmail.com";

	
	@Test
	void insertUser_return201() throws Exception{
		UserResponse response = new UserResponse(
				UUID.randomUUID(),
				"juan",
				email,
				LocalDateTime.now(),
				LocalDateTime.now(),
				LocalDateTime.now(),
				"token-falso",
				true,
				List.of()
		);
		
		// 2. Mockear el servicio para que devuelva la respuesta
		when(service.createUser(any())).thenReturn(response);
		
		// 3. Mockear el JWT, para que el filtro de seguridad lo valide
		final String validToken = "token-valido-para-prueba";
		when(jwtUtil.validateToken(validToken)).thenReturn(true);
		when(jwtUtil.extractUsername(validToken)).thenReturn(email);
		
		// 4. Realizar la llamada con MockMvc, incluyendo la cabecera de autorización
		mockMvc.perform(post("/users")
				.header("Authorization", "Bearer " + validToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
							"name": "juan",
							"email": "juan@gmail.com",
							"password": "mIcontraseña123",
							"phones": 
							[
								{
									"number": "12345678",
									"citycode": "1",
									"contrycode": "59"
								},
								{
									"number": "87654321",
									"citycode": "1",
									"contrycode": "59"
								}
			                ]
			            }
			            """))
			    .andExpect(status().isCreated())
			    .andExpect(jsonPath("$.email").value(email));
	}

	@Test
    void getUserById_return200() throws Exception {
		final UUID id = UUID.randomUUID();
		final String validToken = "token-de-prueba-valido";
		final String username = "admin@bci.cl";
		
		UserResponse response = new UserResponse(
				id,
				"juan",
				username,
				LocalDateTime.now(),
				LocalDateTime.now(),
				LocalDateTime.now(),
				validToken,
				true,
				List.of()
		);
		
		when(jwtUtil.validateToken(validToken)).thenReturn(true);
		when(jwtUtil.extractUsername(validToken)).thenReturn(username);
		
		when(jwtUtil.extractRoles(validToken)).thenReturn(List.of("ADMIN"));
		when(service.findById(id)).thenReturn(response);
		
		mockMvc.perform(get("/users/{id}", id)
				.header("Authorization", "Bearer " + validToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id.toString()));
	}
	
	
	@Test
	void getAllUsers_return200() throws Exception {
		UUID id = UUID.randomUUID();
		final String validToken = "token-falso";
		final String username = "juan@gmail.com";
		
		List<UserResponse> userList = List.of(
				new UserResponse(
						id,
						"juan",
						username,
						LocalDateTime.now(),
						LocalDateTime.now(),
						LocalDateTime.now(),
						validToken,
						true,
						List.of()
				)
		);
		
		when(jwtUtil.validateToken(validToken)).thenReturn(true);
		when(jwtUtil.extractUsername(validToken)).thenReturn(username);
		
		when(jwtUtil.extractRoles(validToken)).thenReturn(List.of("ADMIN"));
		when(service.findAll()).thenReturn(userList);
		
		mockMvc.perform(get("/users")
				.header("Authorization", "Bearer " + validToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(id.toString()))
				.andExpect(jsonPath("$[0].name").value("juan"))
				.andExpect(jsonPath("$[0].email").value(username));
	}
	
	
	@Test
	void updateUser_return200() throws Exception {
		UUID id = UUID.randomUUID();
		final String validToken = "token-de-prueba-valido"; // Define el token aquí
		
		when(jwtUtil.validateToken(validToken)).thenReturn(true);
		when(jwtUtil.extractUsername(validToken)).thenReturn("juan@gmail.com");
		
		UserResponse response = new UserResponse(
				id,
				"juan",
				"juan@gmail.com",
				LocalDateTime.now(),
				LocalDateTime.now(),
				LocalDateTime.now(),
				validToken,true,
				List.of()
		);
		
		when(service.update(eq(id), any(UserRequest.class))).thenReturn(response);
		
		mockMvc.perform(put("/users/{id}", id)
				.header("Authorization", "Bearer " + validToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{
						"name": "juan",
						"email": "juan@gmail.com",
						"password": "mIContraseña123",
						"phones": [
							{
								"number": "12345678",
								"citycode": "1",
								"contrycode": "59"
							}
						]
					}
				"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id.toString()))
				.andExpect(jsonPath("$.email").value("juan@gmail.com"))
				.andExpect(jsonPath("$.name").value("juan"));
	}

	
	@Test
	void createUser_badRequest_400_invalidEmail() throws Exception {
		final String validToken = "token-de-prueba-valido";
		
		when(jwtUtil.validateToken(validToken)).thenReturn(true);
		when(jwtUtil.extractUsername(validToken)).thenReturn("admin@bci.cl");
		
		mockMvc.perform(post("/users")
				.header("Authorization", "Bearer " + validToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\": \"juan\", \"email\": \"correo-no-valido\", \"password\": \"Clave12345\", \"phones\": [] }"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.mensaje").exists());
	}
	
	
	@Test
	void accessDenied_return403Message() throws Exception{
		UUID id = UUID.randomUUID();
		
		when(jwtUtil.validateToken("token-falso")).thenReturn(true);
		when(jwtUtil.extractUsername("token-falso")).thenReturn("usuario-prueba");
		
		doThrow(new AccessDeniedException("No Autorizado")).when(service).findById(id);
		
		mockMvc.perform(get("/users/{id}", id)
				.header("Authorization", "Bearer token-falso"))
				.andExpect(status().isForbidden())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.mensaje").value("No tienes acceso a este recurso"));
	}
}
