package com.hotel.checkout.config;


@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Checkout API")
                        .version("1.0")
                        .description("API para gestionar el proceso de checkout en un hotel"));
    }

}
