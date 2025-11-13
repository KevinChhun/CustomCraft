package com.example.customcraft.floatingUI
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.customcraft.R


@Composable
fun TopBar(navController: NavController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp, bottom = 16.dp)
    ) {
        IconButton(
            onClick = {}
        ) {
            Icon(Icons.Filled.Menu, contentDescription = null)

        }
        SearchBar()
        IconButton(onClick = {navController.navigate("chatrooms")}) {
            Icon(
                painterResource(R.drawable.baseline_chat_24),
                contentDescription = null
            )
        }
    }
}

