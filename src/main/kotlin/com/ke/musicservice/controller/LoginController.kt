package com.ke.musicservice.controller

import com.ke.music.api.HttpService
import com.ke.musicservice.config.JwtManager
import com.ke.musicservice.cookie
import com.ke.musicservice.entity.User
import com.ke.musicservice.entity.UserRole
import com.ke.musicservice.entity.vo.BaseVO
import com.ke.musicservice.entity.vo.CreateLoginKeyVO
import com.ke.musicservice.entity.vo.LoginVO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "登录")
@RestController
class LoginController(private val jwtManager: JwtManager, private val httpService: HttpService) {


	@Operation(summary = "创建登录key及网易云音乐app需要扫描的二维码")
	@GetMapping("/key")
	suspend fun createLoginKey(): BaseVO<CreateLoginKeyVO> {

		val key = httpService.createQRKey().data!!.unikey!!
		val url = httpService.createQRUrl(key).data!!.qrurl!!

		return BaseVO.success(CreateLoginKeyVO(key, url))
	}

	@Operation(summary = "用app扫码完成之后调用这个接口")
	@GetMapping("/login")
	suspend fun login(
		@Parameter(description = "上一步返回的key", required = true)
		@RequestParam key: String
	): BaseVO<LoginVO> {
		val response = httpService.checkLoginByKey(key)

		if (response.code != 803) {
			//检查登录失败
			return BaseVO.error(response.message, code = response.code)
		}

		val cookie = response.cookie

		val userId = httpService.loginStatus(cookie).data!!.profile!!.userId


		val user = User(userId.toString(), cookie = response.cookie, roles = listOf(UserRole.ROLE_USER))
		val token = jwtManager.createToken(user)
		return BaseVO.success(LoginVO(token, user.id, user.roles))
	}


	@Operation(summary = "判断登录状态是否还在", description = "有可能Cookie过期了，建议启动的时候先调用下这个接口")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/login/status")
	suspend fun loginStatus(
		authentication: Authentication
	): BaseVO<Long> {

		val cookie = authentication.cookie

		val userId = httpService.loginStatus(cookie).data?.profile?.userId

		return if (userId != null) {
			BaseVO.success(userId)
		} else {
			BaseVO.error("登录信息失效")
		}
//		val response = httpService.getUserPlaylistList(userId = user.username.toLong(), cookie = cookie)

//		return response
	}
}