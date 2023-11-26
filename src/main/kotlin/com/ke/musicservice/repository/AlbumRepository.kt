package com.ke.musicservice.repository

import com.ke.musicservice.entity.document.Album
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AlbumRepository : CoroutineCrudRepository<Album, Long>

suspend fun AlbumRepository.saveIfNotExist(album: com.ke.music.api.response.Album) {
    if (existsById(album.id)) {
        return
    }
    save(Album(album.id, album.name, album.imageUrl))
}