package cl.bci.common.helper;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API para registro de usuarios",
        version = "1.0",
        description = "Gestión de autenticación de usuarios con token JWT",
        contact = @Contact(name = "Alvaro Vergara", email = "alvaro.vergara.cl@gmail.com")
    )
)
public class SwaggerConfig {

}
