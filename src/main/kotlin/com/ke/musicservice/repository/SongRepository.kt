package com.ke.musicservice.repository

import com.ke.musicservice.entity.document.Song
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SongRepository : CoroutineCrudRepository<Song, Long>