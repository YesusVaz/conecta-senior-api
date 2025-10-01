package br.com.conectasenior.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do OpenAPI/Swagger para documentação da API
 *
 * Define informações da API, esquema de segurança JWT e metadados
 * para gerar documentação interativa acessível em /swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI conectaSeniorOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Conecta Senior API")
                        .description("API REST para a plataforma Conecta Senior - Sistema de cuidado e monitoramento para idosos")
                        .version("v1.0.0"))
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    /**
     * Configura o esquema de autenticação JWT para o Swagger
     */
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}
