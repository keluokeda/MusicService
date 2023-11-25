package com.ke.musicservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.GetMapping

@EnableReactiveMethodSecurity
@SpringBootApplication
class MusicServiceApplication {

	@GetMapping("/")
	suspend fun greet(): Map<String, String> = mapOf("message" to "Hello World")
}

fun main(args: Array<String>) {
	runApplication<MusicServiceApplication>(*args)
}

val Authentication.user: User
	get() = this.principal as User

val Authentication.cookie: String
	get() = this.credentials.toString()