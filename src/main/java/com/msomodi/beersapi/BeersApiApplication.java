package com.msomodi.beersapi;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BeersApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(BeersApiApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI(@Value("v1") String appVersion) {
		return new OpenAPI()
				.components(new Components())
				.info(new Info().title("Beer API").version(appVersion)
						.license(new License().name("MIT").url("MIT")));
	}
}

