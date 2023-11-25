package com.ke.musicservice.entity.vo

import com.ke.musicservice.entity.UserRole

data class LoginVO(
	val token: String,
	val id: String,
	val roles: List<UserRole>
)
