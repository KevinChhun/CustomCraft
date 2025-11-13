package com.example.customcraft.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


@Composable
fun ChannelsScreen(navController: NavController) {
    val viewModel = hiltViewModel<ChannelsViewModel>()
    val channels = viewModel.channels.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "back"
                    )
                }
                Text(
                    text = "Chat Rooms",
                    fontSize = 26.sp
                )
            }
            HorizontalDivider()
            TextField(value = "",
                onValueChange = {},
                placeholder = { Text(text = "Search...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, vertical = 8.dp)
                    .background(Color.White),
                textStyle = TextStyle(color = Color.LightGray),
                colors = TextFieldDefaults.colors().copy(
                    // Code to change colors :D
                    //focusedContainerColor = DarkGrey,
                    //unfocusedContainerColor = DarkGrey,
                    //focusedTextColor = Color.Gray,
                    //unfocusedTextColor = Color.Gray,
                    //focusedPlaceholderColor = Color.Gray,
                    //unfocusedPlaceholderColor = Color.Gray,
                    //focusedIndicatorColor = Color.Gray
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null
                    )
                }
            )
            LazyColumn {
                items(channels.value) { channel ->
                    val channelName = viewModel.getChannelName(channel)
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 2.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .clickable { navController.navigate("chat/${channel.id}/${channelName}") },
                            verticalAlignment = Alignment.CenterVertically

                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(70.dp)
                                    .clip(CircleShape)
                                    .background(Color.Blue.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    text = channelName[0].uppercase(),
                                    color = Color.White,
                                    style = TextStyle(fontSize = 35.sp),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            Text(text = channelName, modifier = Modifier.padding(8.dp))

                        }
                    }
                }
            }
//            Button(
//                onClick = { viewModel.createTestChannel() }
//            ) {
//                Text(
//                    text = "Add Channel"
//                )
//            }
        }
    }
}

