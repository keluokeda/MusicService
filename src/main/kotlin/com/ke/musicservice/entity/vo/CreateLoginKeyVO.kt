package com.ke.musicservice.entity.vo

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "登录key及网易云音乐app需要扫描的二维码")
data class CreateLoginKeyVO(
	@Schema(description = "登录key")
	val key: String,
	@Schema(description = "前端需要把这个url转换成二维码并用app扫描")
	val url: String
)
