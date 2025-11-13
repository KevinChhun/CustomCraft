package com.example.customcraft.model

data class Channel(
    val id: String = "",
    val members: Map<String, String> = mapOf("" to ""),
    val createdAt: Long = System.currentTimeMillis()
)