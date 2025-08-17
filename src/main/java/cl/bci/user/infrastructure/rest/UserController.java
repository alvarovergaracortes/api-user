package cl.bci.user.infrastructure.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.bci.user.application.service.UserServicePort;
import cl.bci.user.infrastructure.rest.dto.UserRequest;
import cl.bci.user.infrastructure.rest.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuarios", description = "Operaciones Crud para los usuarios")
public class UserController {
	
	private final UserServicePort service;
	
	public UserController(UserServicePort service) {
		this.service = service;
	}
	
	@Operation(summary = "Inserta los datos de un User", description = "Crea un nuevo usuario con los enviados")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Creacion del user exitosa"),
		@ApiResponse(responseCode = "404", description = "Proveedor no encontrado"),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
	@PostMapping
	public ResponseEntity<UserResponse> insert(@RequestBody @Valid UserRequest userRequest) {
		UserResponse userResponse = service.createUser(userRequest);
		return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
	}
	
	
	@Operation(summary = "Actualiza los datos de un User", description = "Actualiza nombe, password, si esta activo, lista de telefonos")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Actualizacion del user exitosa"),
		@ApiResponse(responseCode = "404", description = "User no encontrado"),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@PutMapping("/{id}")
	public ResponseEntity<UserResponse> update(@PathVariable("id") UUID id, @RequestBody @Valid UserRequest userRequest) {
		UserResponse updatedUser = service.update(id, userRequest);
		return ResponseEntity.ok(updatedUser);
	}
	
	
	@Operation(summary = "Obtiene una lista de User", description = "Obtiene todos los usuarios registrados en la BD")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Lsta de users exitosa"),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@GetMapping
	public ResponseEntity<List<UserResponse>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	
	@Operation(summary = "Obtiene un User", description = "Busca un usuario en particular por ID")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "busqueda de User exitosa"),
		@ApiResponse(responseCode = "404", description = "User no encontrado"),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> findById(@PathVariable("id") UUID id) {
		UserResponse usuario = service.findById(id);
		return ResponseEntity.ok(usuario);
	}
	
	
	@Operation(summary = "Elimina un User", description = "Elimina un usuario de la BD")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Eliminacion de User exitosa"),
		@ApiResponse(responseCode = "404", description = "User no encontrado"),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}

