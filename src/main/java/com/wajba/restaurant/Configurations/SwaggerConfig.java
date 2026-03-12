package com.wajba.restaurant.Configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Restaurant Management API")
                        .description("A comprehensive REST API for restaurant delivery management system - add menu items, manage orders, and track deliveries")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Wajba Restaurant Team")
                                .url("https://wajba.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
