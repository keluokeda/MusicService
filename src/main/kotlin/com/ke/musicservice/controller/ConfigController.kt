package com.ke.musicservice.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ConfigController {

	@Value("\${com.ke.music.api.url}")
	lateinit var baseUrl: String


	@GetMapping("/config")
	fun getConfig(): Map<String, Any> {
		return mapOf("baseUrl" to baseUrl)
	}
}