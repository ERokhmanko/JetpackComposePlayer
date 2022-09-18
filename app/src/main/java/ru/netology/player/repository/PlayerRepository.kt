package ru.netology.player.repository

import androidx.lifecycle.LiveData
import retrofit2.Response
import ru.netology.player.dto.Album

interface PlayerRepository {
    suspend fun getAll():Album
}