package com.ke.musicservice.entity.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Song(
	@Id
	val id: Long,
	val name: String,
	val albumId: Long,
	val mv: Long
)
