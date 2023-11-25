package com.ke.musicservice.entity.vo

data class BaseVO<T>(
	val code: Int,
	val success: Boolean,
	val message: String,
	val data: T?
) {
	companion object {
		fun <T> success(data: T?): BaseVO<T> {
			return BaseVO(200, true, "success", data)
		}

		fun <T> error(message: String, code: Int = 1000): BaseVO<T> {
			return BaseVO(code, false, message, null)
		}
	}
}
