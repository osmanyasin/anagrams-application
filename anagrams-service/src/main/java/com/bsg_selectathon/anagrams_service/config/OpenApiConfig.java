package com.bsg_selectathon.anagrams_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI anagramsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Anagrams Service")
                        .description("""
                                REST API for managing a dictionary and computing anagrams.
                                
                                Supports:
                                - Adding and deleting words
                                - Retrieving all words
                                - Finding anagrams for a given word
                                - Counting anagrams grouped by word length across the entire dictionary
                                """)
                        .version("v0.0.1")
                        .contact(new Contact()
                                .name("Portfolio Project")
                                .email("osmanyasin0077@gmail.com"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local development server")
                ));
    }
}
