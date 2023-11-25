package com.ke.musicservice.repository

import com.ke.musicservice.entity.document.Playlist
import com.ke.musicservice.entity.document.toDocument
import kotlinx.coroutines.flow.collect
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PlaylistRepository : CoroutineCrudRepository<Playlist, Long>

suspend fun PlaylistRepository.saveAll(playlists: List<com.ke.music.api.response.Playlist>) {
	val list = playlists.map { playlist ->
		val target = findById(playlist.id)
		playlist.toDocument(
			target?.shareCount ?: 0,
			target?.bookedCount ?: 0,
			target?.commentCount ?: 0
		)
	}
	saveAll(list).collect()
}