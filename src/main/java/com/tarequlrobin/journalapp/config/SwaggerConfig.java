package com.tarequlrobin.journalapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myCustomConfig(){
        return new OpenAPI()
                .info(
                new Info().title("Learning Spring Boot")
                        .description("By Robin")
                )
                .servers(Arrays.asList(
                        new Server().url("http://localhost:8080").description("local"),
                        new Server().url("http://localhost:8081").description("live")))
                .tags(Arrays.asList(
                        new Tag().name("Public APIs"),
                        new Tag().name("UserDTO APIs"),
                        new Tag().name("Journal Entry APIs"),
                        new Tag().name("Admin APIs")
                ));

    }
}
