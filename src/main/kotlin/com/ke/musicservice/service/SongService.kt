package com.ke.musicservice.service

import com.ke.music.api.response.Song
import com.ke.musicservice.entity.document.Album
import com.ke.musicservice.entity.document.ArtistSong
import com.ke.musicservice.repository.*
import kotlinx.coroutines.flow.collect
import org.springframework.stereotype.Service

@Service
class SongService(
    private val songRepository: SongRepository,
    private val artistRepository: ArtistRepository,
    private val albumRepository: AlbumRepository,
    private val artistSongRepository: ArtistSongRepository
) {

    /**
     * 保存歌曲
     */
    suspend fun saveSongs(songs: List<Song>) {

        val songDocuments = songs.map { song ->
            val artistSongsList = song.artists.map { artist ->
                //保存歌手
                artistRepository.saveIfNotExist(artist)
                ArtistSong(null, artist.id, song.id)
            }
            //保存歌手歌曲之间的关系
            artistSongRepository.saveAll(artistSongsList).collect()

            albumRepository.saveIfNotExist(song.album)

            com.ke.musicservice.entity.document.Song(song.id, song.name, song.album.id, song.mv)
        }
        songRepository.saveAll(songDocuments).collect()
    }
}