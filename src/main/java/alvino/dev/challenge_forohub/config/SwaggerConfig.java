package alvino.dev.challenge_forohub.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ForoHub API")
                        .version("1.0")
                        .description("API REST del challenge ForoHub - Alura/ONE\n\n" +
                                "Usa el botón 'Authorize' (candado superior derecho) para ingresar el token JWT.\n" +
                                "Formato: Bearer <tu-token>"))
                // Componente de seguridad global
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Ingresa tu token JWT aquí (obtenido en POST /login)")))
                // Aplica la seguridad globalmente (todos los endpoints la requieren por defecto)
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
