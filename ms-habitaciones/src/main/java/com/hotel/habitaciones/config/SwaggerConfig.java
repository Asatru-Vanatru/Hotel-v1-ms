package com.hotel.habitaciones.config;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Habitaciones del Hotel")
                        .version("1.0")
                        .description("API REST para gestionar las habitaciones del hotel, incluyendo creación, actualización, eliminación y consulta de habitaciones."))
    }

}
