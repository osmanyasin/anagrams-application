package bsg.application.anagrams;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BSG Anagrams Application")
                        .version("1.0.0")
                        .description("This is a BSG Anagrams Application Spring Boot application with Swagger UI integration"));
    }
}
