package ru.netology.player.dto

data class Track(
    val id: Long,
    val file: String,
    val titleAlbum: String?,
    val running: Boolean = false
)