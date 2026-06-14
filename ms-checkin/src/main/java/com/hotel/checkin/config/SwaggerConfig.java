package com.hotel.checkin.config;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Check-in API")
                        .version("1.0")
                        .description("API para gestionar el proceso de check-in en un hotel"));
    }

}
