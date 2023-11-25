package com.ke.musicservice.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import reactor.core.publisher.Mono


@Configuration
class SecurityConfig {


	@Bean
	fun filterChain(
		serverHttpSecurity: ServerHttpSecurity,
		jwtTokenAuthenticationFilter: JwtTokenAuthenticationFilter
	): SecurityWebFilterChain {
		return serverHttpSecurity
			.csrf {
				it.disable()
			}
			.httpBasic {
				it.disable()
			}
			.formLogin {
				it.disable()
			}
			.exceptionHandling {
				//没有权限访问
				it.accessDeniedHandler { exchange, denied ->
					val response = exchange.response
					response.statusCode = HttpStatus.OK
					response.headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					val map = mapOf(
						"code" to HttpStatus.FORBIDDEN.value(),
						"message" to "无权限访问"
					)

					response.writeWith(
						Mono.just(
							response.bufferFactory().wrap(
								ObjectMapper().writeValueAsBytes(map)
							)
						)
					)
				}
				//没有登录
				it.authenticationEntryPoint { exchange, ex ->
					val response = exchange.response
					response.statusCode = HttpStatus.OK
					response.headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					val map = mapOf(
						"code" to HttpStatus.UNAUTHORIZED.value(),
						"message" to "未登录或登录超时"
					)

					response.writeWith(
						Mono.just(
							response.bufferFactory().wrap(
								ObjectMapper().writeValueAsBytes(map)
							)
						)
					)
				}
			}
			.authorizeExchange {
				it.pathMatchers("/key").permitAll()//这个地址不拦截
				it.pathMatchers("/login").permitAll()//登录不要拦截
				it.pathMatchers("/config").permitAll()
				//spring doc配置
				it.pathMatchers("/swagger-ui/index.html").permitAll()
				it.pathMatchers("/swagger-ui/**").permitAll()
				it.pathMatchers("/swagger-ui.html").permitAll()
				it.pathMatchers("/swagger-resources/**").permitAll()
				it.pathMatchers("/v3/api-docs/**").permitAll()
				it.pathMatchers("/webjars/**").permitAll()
				it.anyExchange().authenticated()//其余的都拦截
			}
			.exceptionHandling {
				it.toString()
			}
			.addFilterAt(jwtTokenAuthenticationFilter, SecurityWebFiltersOrder.HTTP_BASIC)
			.build()
	}


}