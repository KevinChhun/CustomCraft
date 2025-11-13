package com.example.customcraft.homePage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.customcraft.MainApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomepageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
        MainApp()
        }
    }
}