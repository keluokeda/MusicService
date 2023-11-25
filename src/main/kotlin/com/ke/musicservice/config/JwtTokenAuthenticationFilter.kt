package com.ke.musicservice.config

import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono


@Component
class JwtTokenAuthenticationFilter(private val jwtManager: JwtManager) : WebFilter {
	override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
		val token = resolveToken(exchange.request)

		if (token != null) {
			val authentication = jwtManager.parseToken(token)
			if (authentication != null) {
				return chain.filter(exchange)
					.contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))

			}
		}

		return chain.filter(exchange)
	}

	private fun resolveToken(request: ServerHttpRequest): String? {
		val bearerToken: String = request.headers.getFirst(org.springframework.http.HttpHeaders.AUTHORIZATION)
			?: return null
		return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
			bearerToken.substring(7)
		} else null
	}

	companion object {
		private const val HEADER_PREFIX = "Bearer "
	}
}