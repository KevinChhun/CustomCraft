package com.example.customcraft.model

data class Rating(
    val artistID: String = "",
    val ratingNumber: Double = 0.0,
    val isComplete: Boolean = false,
    val ratingInProgress: Boolean = false
)