package com.ke.musicservice.entity.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class ArtistSong(
	@Id
	val id: String? = null,
	val artistId: Long,
	val songId: Long
)
