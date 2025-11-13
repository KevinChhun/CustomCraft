package com.example.customcraft.model

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class ArtistCardData(
    val artistData: UserData,
    val spotlightImg: MutableState<Bitmap?> = mutableStateOf(null),
    val profileImg: MutableState<Bitmap?> = mutableStateOf(null)
)
