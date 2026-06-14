package com.hotel.consumos.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Consumos API")
                        .version("1.0")
                        .description("API para gestionar los consumos de los huéspedes en el hotel"));
    }

}
