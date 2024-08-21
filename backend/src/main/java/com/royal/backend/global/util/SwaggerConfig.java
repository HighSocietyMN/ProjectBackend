package com.royal.backend.global.util;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "HiSociety API Document",
                description = "HiSociety API 명세서",
                version = "1.0.0"
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Server"),
                @Server(url = "http://192.168.1.31:8080", description = "Dev Server"),
                @Server(url = "http://192.168.43.180:8080", description = "My Server")
        }
)
public class SwaggerConfig {
}
