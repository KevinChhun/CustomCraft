package com.example.customcraft.model

data class User(
    val id: String = "",
    val username: String = "",
    val description: String = "",
    val profileImgURL: String = "", // "username/filename"
    val spotlightImgURL: String = "", // "username/filename"
    val galleryImgURLs: List<String> = listOf(""), // "username/filename"
    val tags: List<String> = listOf(""), // capitalized and includes hashtag ie "#Rings",
    val isArtist: Boolean = false,
    val onShowcase: Boolean = false
)