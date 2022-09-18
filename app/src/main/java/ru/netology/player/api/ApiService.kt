package ru.netology.player.api


import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import ru.netology.player.BuildConfig
import ru.netology.player.dto.Album

private const val BASE_URL = BuildConfig.BASE_URL

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface ApiService {
    @GET("album.json")
    suspend fun getAll(): Response<Album>

}

object Api {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}