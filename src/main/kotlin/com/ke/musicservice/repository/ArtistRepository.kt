package com.ke.musicservice.repository

import com.ke.music.api.response.Singer
import com.ke.musicservice.entity.document.Artist
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ArtistRepository : CoroutineCrudRepository<Artist, Long>


/**
 * 如果歌手不存在就保存歌手
 */
suspend fun ArtistRepository.saveIfNotExist(singer: Singer) {
    if (existsById(singer.id)) {
        return
    }
    save(Artist(singer.id, singer.name))
}