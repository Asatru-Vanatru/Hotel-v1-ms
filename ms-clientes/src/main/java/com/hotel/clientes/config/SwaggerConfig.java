package com.hotel.clientes.config;


@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Clientes del Hotel")
                        .version("1.0")
                        .description("API para gestionar clientes del hotel"))


    }
}
