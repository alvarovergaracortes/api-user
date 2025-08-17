package cl.bci.infrastructure.rest;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.bci.common.exception.InvalidJwtAuthenticationException;
import cl.bci.common.exception.UserNotFoundException;
import cl.bci.user.application.service.LoginServicePort;
import cl.bci.user.domain.User;
import cl.bci.user.infrastructure.persistence.mapper.UserMapper;
import cl.bci.user.infrastructure.rest.LoginController;
import cl.bci.user.infrastructure.rest.dto.LoginRequest;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(cl.bci.common.exception.GlobalExceptionHandler.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoginServicePort loginService;

    @MockitoBean
    private UserMapper userMapper;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockitoBean
    private cl.bci.common.security.JwtUtil jwtUtil;

    @MockitoBean
    private cl.bci.common.security.AuthenticationFilter authenticationFilter;

    private static final String LOGIN_URL = "/auth/login";

    @Test
    @DisplayName("POST /auth/login -> 200 OK con token")
    void login_ok() throws Exception {
    	LoginRequest request = new LoginRequest(
    	        "valid.user@example.com",
    	        "Password123"
    	    );
    	    String body = objectMapper.writeValueAsString(request);

    	    // Stubs del mapper y servicio
    	    String email = "valid.user@example.com";
    	    String token = "jwt-token-123";

    	    User domainUser = Mockito.mock(User.class);
    	    when(domainUser.getEmail()).thenReturn(email);

    	    when(userMapper.toUser(any())).thenReturn(domainUser);
    	    when(loginService.login(domainUser)).thenReturn(Optional.of(token));

    	    // Act & Assert
    	    mockMvc.perform(post("/auth/login")
    	            .contentType(MediaType.APPLICATION_JSON)
    	            .accept(MediaType.APPLICATION_JSON)
    	            .content(body))
    	        .andDo(print())
    	        .andExpect(status().isOk())
    	        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
    	        .andExpect(jsonPath("$.email").value(email))
    	        .andExpect(jsonPath("$.token").value(token));

    	    // Verificaciones (opcional pero recomendado)
    	    verify(userMapper).toUser(any());
    	    verify(loginService).login(domainUser);
    	    verifyNoMoreInteractions(userMapper, loginService);
    }

    @Test
    @DisplayName("POST /auth/login -> 500 cuando no se genera token (IllegalStateException)")
    void login_falla_token_no_generado() throws Exception {
    	String email = "john.doe@example.com";
        

        User domainUser = Mockito.mock(User.class);
        when(domainUser.getEmail()).thenReturn(email);

        when(userMapper.toUser(any())).thenReturn(domainUser);
        when(loginService.login(domainUser)).thenReturn(Optional.empty()); // fuerza IllegalStateException

        mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("""
                    {"email":"john.doe@example.com","password":"Str0ng-Passw0rd!"}
                """))
            .andExpect(status().isInternalServerError())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().string(containsString("Error interno:")))
            .andExpect(content().string(containsString("Token no pudo ser generado")));

        verify(userMapper).toUser(any());
        verify(loginService).login(domainUser);
        verifyNoMoreInteractions(userMapper, loginService);
    }

    @Test
    @DisplayName("POST /auth/login -> 400 cuando la validación falla (MethodArgumentNotValidException)")
    void login_bad_request_por_validacion() throws Exception {
        String body = """
            {"email":"","password":"x"}
        """;

        mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(body))
            // .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().string(containsString("email")));

        verifyNoInteractions(userMapper, loginService);
    }

    @Test
    @DisplayName("POST /auth/login -> 404 cuando el usuario no existe (UserNotFoundException)")
    void login_user_not_found() throws Exception {
    	String body = """
    	        {"email":"no.existe@dominio.cl","password":"Aa1!aaaa"}
    		    """;

    		    when(userMapper.toUser(any())).thenReturn(Mockito.mock(User.class));
    		    when(loginService.login(any(User.class)))
    		        .thenThrow(new UserNotFoundException("Usuario no encontrado"));

    		    mockMvc.perform(post("/auth/login")
    		            .contentType(MediaType.APPLICATION_JSON)
    		            .accept(MediaType.APPLICATION_JSON)
    		            .content(body))
    		        //.andDo(print()) // descomenta para ver el JSON real
    		        .andExpect(status().isNotFound())
    		        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
    		        .andExpect(content().string(containsString("Usuario no encontrado")));

    		    verify(userMapper).toUser(any());
    		    verify(loginService).login(any(User.class));
    		    verifyNoMoreInteractions(userMapper, loginService);
    }

    @Test
    @DisplayName("POST /auth/login -> 401 cuando JWT inválido (InvalidJwtAuthenticationException)")
    void login_invalid_jwt() throws Exception {

        when(userMapper.toUser(any())).thenReturn(Mockito.mock(User.class));
        when(loginService.login(any(User.class)))
            .thenThrow(new InvalidJwtAuthenticationException("Token inválido"));

        mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("""
                    {"email":"valid.user@example.com","password":"Str0ng-Passw0rd!"}
                """))
            .andExpect(status().isUnauthorized())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().string(containsString("Token inválido")));

        verify(userMapper).toUser(any());
        verify(loginService).login(any(User.class));
        verifyNoMoreInteractions(userMapper, loginService);
    }

    @Test
    @DisplayName("POST /auth/login -> 403 cuando acceso denegado (AccessDeniedException)")
    void login_access_denied() throws Exception {
    	String body = """
    	        {"email":"valid.user@example.com","password":"Str0ng-Passw0rd!"}
    	    """;

    	    when(userMapper.toUser(any())).thenReturn(Mockito.mock(User.class));
    	    when(loginService.login(any(User.class)))
    	        .thenThrow(new AccessDeniedException("Rol no autorizado"));

    	    mockMvc.perform(post("/auth/login")
    	            .contentType(MediaType.APPLICATION_JSON)
    	            .accept(MediaType.APPLICATION_JSON)
    	            .content(body))
    	        .andExpect(status().isForbidden())
    	        .andExpect(content().string(containsString("No tienes acceso a este recurso")));

    	    verify(userMapper).toUser(any());
    	    verify(loginService).login(any(User.class));
    	    verifyNoMoreInteractions(userMapper, loginService);
    }
}
