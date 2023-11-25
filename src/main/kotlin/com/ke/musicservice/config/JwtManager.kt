package com.ke.musicservice.config

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtManager {

	/**
	 * 创建token
	 */
	fun createToken(user: com.ke.musicservice.entity.User): String {
		val claims = Jwts.claims().setSubject(user.id)
		if (user.roles.isNotEmpty()) {
			claims[AUTHORITIES_KEY] = user.roles.joinToString(",") {
				it.name
			}
		}
		claims["Cookie"] = user.cookie

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(Date())
			.setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 30L))
			.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
			.compact()
	}


	/**
	 * 创建token
	 */
//	fun createToken(authentication: Authentication): String {
//		val claims = Jwts.claims().setSubject(authentication.name)
//		if (authentication.authorities.isNotEmpty()) {
//			claims[AUTHORITIES_KEY] = authentication.authorities.map {
//				it.authority
//			}.joinToString { "," }
//		}
//
//		return Jwts.builder()
//			.setClaims(claims)
//			.setIssuedAt(Date())
//			.setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24L))
//			.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
//			.compact()
//	}

	/**
	 * 解析token
	 */
	fun parseToken(token: String): Authentication? {
		try {
			val body = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).body ?: return null
			val authoritiesClaim = body[AUTHORITIES_KEY]
			val authorities =
				if (authoritiesClaim == null) emptyList<GrantedAuthority>() else AuthorityUtils.commaSeparatedStringToAuthorityList(
					authoritiesClaim.toString()
				)


			val user = User(body.subject, "", authorities)


			return UsernamePasswordAuthenticationToken(user, body["Cookie"], authorities)

		} catch (e: Exception) {
			return null
		}
	}

	companion object {
		private const val AUTHORITIES_KEY = "roles"
		private val SECRET_KEY =
			Base64.getEncoder().encode("1111111111111111111111111111111111111111111111111111111111111".toByteArray())

	}
}