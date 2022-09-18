package ru.netology.player.repository

import ru.netology.player.api.Api
import ru.netology.player.dto.Album
import ru.netology.player.error.ApiError
import ru.netology.player.error.NetworkError
import ru.netology.player.error.UnknownError
import java.io.IOException

class PlayerRepositoryImpl : PlayerRepository {
    override suspend fun getAll():Album {
        try {
            val response = Api.retrofitService.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw Exception()
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}