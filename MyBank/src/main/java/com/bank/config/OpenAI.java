package com.bank.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAI {

    @Bean
    public OpenAPI openAIClient() {
        return new OpenAPI()
                .info(new Info().title("Banking App API")
                        .description("API for Reddit clone Application")
                        .version("v0.0.1")
                        .license(new License().name("Meet License Version 2.0").url("https://code-with-meet.firebaseapp.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Expense Tracker Wiki Documentation")
                        .url("https://code-with-meet.firebaseapp.com"));


    }
}
