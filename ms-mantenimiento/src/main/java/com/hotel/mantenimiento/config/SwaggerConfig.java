package com.hotel.mantenimiento.config;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Mantenimiento API")
                        .version("1.0")
                        .description("API para gestionar el mantenimiento de habitaciones en el hotel"));
    }

}
