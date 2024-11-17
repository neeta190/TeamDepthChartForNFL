package com.trading.solutions.sports.teamDepthcharts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@OpenAPIDefinition()
public class SwaggerConfig {
   @Bean
   public OpenAPI swaggerApiConfig() {
       var info = new Info()
               .title("Team Depth Chart API for NFL")
               .description("Project to"
               		+ " demonstrate the list of apis exposed for Team Depth Chart for NFL Players")
               .version("1.0");
       return new OpenAPI().info(info);
   }
}
