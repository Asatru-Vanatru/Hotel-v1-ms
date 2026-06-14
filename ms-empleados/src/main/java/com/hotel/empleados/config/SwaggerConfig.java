package com.hotel.empleados.config;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Empleados API")
                        .version("1.0")
                        .description("API para la gestión de empleados del hotel"));
    }

}
