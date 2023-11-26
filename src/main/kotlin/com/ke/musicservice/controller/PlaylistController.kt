package com.ke.musicservice.controller

import com.ke.music.api.HttpService
import com.ke.music.api.response.Playlist
import com.ke.musicservice.cookie
import com.ke.musicservice.entity.document.toDocument
import com.ke.musicservice.entity.vo.BaseVO
import com.ke.musicservice.entity.vo.PlaylistDetailVO
import com.ke.musicservice.repository.PlaylistRepository
import com.ke.musicservice.repository.saveAll
import com.ke.musicservice.service.SongService
import com.ke.musicservice.user
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@Tag(name = "歌单")
@RestController
class PlaylistController(
    private val httpService: HttpService,
    private val playlistRepository: PlaylistRepository,
    private val songService: SongService
) {


    @Operation(summary = "获取当前用户的歌单")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/user/current/playlists")
    suspend fun currentUserPlaylists(
        authentication: Authentication
    ): BaseVO<List<Playlist>> {
        val cookie = authentication.cookie
        val userId = authentication.user.username.toLong()

        val list = httpService.getUserPlaylistList(userId, cookie = cookie).playlist

        playlistRepository.saveAll(list)

        return BaseVO.success(list)
    }


    @Operation(summary = "获取某个用户的歌单")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/user/{userId}/playlists")
    suspend fun userPlaylists(
        @Parameter(description = "用户id", required = true, example = "8059600021")
        @PathVariable userId: Long,
        authentication: Authentication
    ): BaseVO<List<Playlist>> {
        val cookie = authentication.cookie

        val list = httpService.getUserPlaylistList(userId, cookie = cookie).playlist

        playlistRepository.saveAll(list)


        return BaseVO.success(list)
    }

    @Operation(summary = "获取歌单详情")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/playlist/{id}")
    suspend fun playlistDetail(
        @Parameter(description = "歌单id", required = true, example = "8264272253")
        @PathVariable id: Long,
        authentication: Authentication
    ) = withContext(Dispatchers.IO) {
        val cookie = authentication.cookie


        val playlist = async {
            httpService.getPlaylistDetail(id, cookie).playlist
        }
        val songs = async {
            httpService.getPlaylistTracks(id, cookie).songs
        }

        val dynamic = async {
            httpService.getPlaylistDetailDynamic(id, cookie)
        }

        BaseVO.success(PlaylistDetailVO(playlist.await(), songs.await(), dynamic.await()))
            .apply {
                playlistRepository.save(
                    this.data!!.playlist.toDocument(
                        this.data.dynamic.shareCount,
                        this.data.dynamic.bookedCount,
                        this.data.dynamic.commentCount
                    )
                )

                songService.saveSongs(data.songs)
            }
    }
}