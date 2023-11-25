package com.ke.musicservice.entity.vo

import com.ke.music.api.response.Playlist
import com.ke.music.api.response.PlaylistDynamicResponse
import com.ke.music.api.response.Song

data class PlaylistDetailVO(
	val playlist: Playlist,
	val songs: List<Song>,
	val dynamic: PlaylistDynamicResponse
)
