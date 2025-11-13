package com.example.customcraft.model

import com.google.firebase.Timestamp

data class Commission(
    val commissionerID: String = "",
    val commissionerName: String = "",
    val commissionerProfilePic:String = "",
    val commissionID: String = "",
    val commission: String = "",
    val money: Double = 0.0,
    val artistID: String = "",
    val artistName: String = "",
    val artistProfilePic:String = "",
    val commissionAccepted: Boolean = false,
    val commissionInProgress: Boolean = false,
    val commissionComplete: Boolean = false,
    val commissionDenied:Boolean = false,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)
