package com.keyconsulting.todolist.app.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8088/");
        devServer.setDescription("Server URL in Development environment");

        Info info = new Info()
                .title("Todo Service API")
                .version("1.0");

      var securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .name("bearer")
                .scheme("bearer")
                .bearerFormat("opaque")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");
        var securityComponent = new Components()
                .addSecuritySchemes("bearer", securityScheme);
        var securityItem = new SecurityRequirement()
                .addList("bearer");

        return new OpenAPI()
                .components(securityComponent)
                .addSecurityItem(securityItem).info(info).servers(List.of(devServer));
    }
}
