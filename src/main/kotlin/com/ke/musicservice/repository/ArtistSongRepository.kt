package com.ke.musicservice.repository

import com.ke.musicservice.entity.document.Artist
import com.ke.musicservice.entity.document.ArtistSong
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ArtistSongRepository : CoroutineCrudRepository<ArtistSong,Long>