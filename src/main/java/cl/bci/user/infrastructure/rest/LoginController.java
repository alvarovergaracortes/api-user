package cl.bci.user.infrastructure.rest;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.bci.user.application.service.LoginServicePort;
import cl.bci.user.domain.User;
import cl.bci.user.infrastructure.persistence.mapper.UserMapper;
import cl.bci.user.infrastructure.rest.dto.LoginRequest;
import cl.bci.user.infrastructure.rest.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "LoginController", description = "Permite realizar login y si esta OK devuelve token de acceso")
@RestController
@RequestMapping("/auth")
public class LoginController {
	
	private final LoginServicePort loginService;
	private final UserMapper userMapper;
	
	public LoginController(LoginServicePort loginService, UserMapper userMapper) {
		this.loginService = loginService;
		this.userMapper = userMapper;
	}


	@Operation(summary = "Login de la api", description = "Permite realizar la autenticacion de usuarios")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Creacion del token exitosa"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "401", description = "Password incorrecta"),
        @ApiResponse(responseCode = "401", description = "Rol no autorizado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
	@PostMapping(
		path = "/login",
		consumes = "application/json",
		produces = "application/json"
	)
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request){
		
    	User user = userMapper.toUser(request);
    	
    	
    	Optional<String> tknOptional = this.loginService.login(user);
    	
    	String token = tknOptional.orElseThrow(
                () -> new IllegalStateException("Error interno: Token no pudo ser generado.")
            );
    	
    	TokenResponse tokenResponse = new TokenResponse(user.getEmail(), token);
        return ResponseEntity.ok(tokenResponse);
    }

}
