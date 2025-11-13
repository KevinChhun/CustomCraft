package com.example.customcraft.model

data class UserData(
    val id: String = "",
    val username: String = "",
    val description: String = "",
    val imgUri: String = "",
    val profileImgURL: String = "",
    val spotlightImgURL: String = "",
    val galleryImgURLs: List<String> = listOf(""),
    val tags: List<String> = listOf(""),
    val artist: Boolean = false,
    val onShowcase: Boolean = false
)
