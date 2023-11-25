package com.ke.musicservice.entity

data class User(
	val id: String,
	val cookie: String,
	val roles: List<UserRole>
)

enum class UserRole {
	/**
	 *普通用户
	 */
	ROLE_USER,

	/**
	 * 管理员
	 */
	ROLE_MANAGER,

	/**
	 *最高权限
	 */
	ROLE_ADMIN,
}
