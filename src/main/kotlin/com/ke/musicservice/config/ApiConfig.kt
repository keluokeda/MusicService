package com.ke.musicservice.config

import com.ke.music.api.HttpService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Configuration
class ApiConfig {


	@Value("\${com.ke.music.api.url}")
	lateinit var baseUrl: String

	private fun okhttpClient(): OkHttpClient {

		val logger = HttpLoggingInterceptor {
			println(it)
		}.apply {
			level = HttpLoggingInterceptor.Level.BODY
		}

		return OkHttpClient.Builder()

			.addInterceptor { chain ->
				val original = chain.request()
				val request = original.newBuilder()
					.url(
						original.url.newBuilder()
							.addQueryParameter("timestamp", System.currentTimeMillis().toString())
							.build()
					)
					.build()

				println(request.url.toString())

				chain.proceed(request)
			}
			.addNetworkInterceptor(logger)
			.build()
	}

	@Bean
	fun createHttpService(): HttpService {
		return Retrofit.Builder()
			.baseUrl(
				baseUrl
//				"http://192.168.31.181:3000/"
//				"http://192.168.10.13:3000/"
//				"https://ke-music.clopar.top/"
			)
			.addConverterFactory(MoshiConverterFactory.create())
			.client(okhttpClient())
			.build()
			.create(HttpService::class.java)
	}
}