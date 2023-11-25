package com.ke.musicservice.config

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@io.swagger.v3.oas.annotations.security.SecurityScheme(
	name = "Bearer Authentication",
	type = SecuritySchemeType.HTTP,
	bearerFormat = "JWT",
	scheme = "bearer"
)
class SpringDocConfig {

	@Bean
	fun customOpenApi(): OpenAPI {
		return OpenAPI()
			.components(
				Components() // 设置 spring security jwt accessToken 认证的请求头 Authorization: Bearer xxx.xxx.xxx
//					.addSecuritySchemes(
//						"Authorization", SecurityScheme()
//							.type(SecurityScheme.Type.APIKEY)
//							.bearerFormat("JWT")
//							.scheme("basic")
//							.description("jwt token")
//					)
			)
	}
}