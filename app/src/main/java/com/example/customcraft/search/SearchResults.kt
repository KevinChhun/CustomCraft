package com.example.customcraft.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size

@Composable
fun SearchResults(
    navController: NavController,
    searchQuery: String,
    viewModel: SearchResultsViewModel = viewModel()
) {
    val artistCardResults = viewModel.artistCardSearchResults.value
    LaunchedEffect(key1 = searchQuery) {
        viewModel.search(searchQuery)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "back"
                    )
                }
                Text(text = searchQuery, fontSize = 26.sp)
            }
            HorizontalDivider()
            LazyColumn {
                items(artistCardResults.size) { item ->
                    val username = artistCardResults[item].artistData.username
                    val description = artistCardResults[item].artistData.description
                    val userUID = artistCardResults[item].artistData.id
                    val profileImage = artistCardResults[item].profileImg.value
                    val profilePainter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(profileImage)
                            .size(Size.ORIGINAL)
                            .build()
                    )
                    if (profilePainter.state is AsyncImagePainter.State.Success) {
                        Card(
                            onClick = { navController.navigate("profile/${userUID}") },
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.Top
                            ) {

                                    Image(
                                        painter = profilePainter,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(72.dp)
                                    )

                                Column(
                                    modifier = Modifier
                                        .padding(4.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        Column {

                                            Text(
                                                text = username,
                                                fontWeight = FontWeight.Bold
                                            )

                                            HorizontalDivider(
                                                modifier = Modifier
                                                    .width(280.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = description,
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier
                                            .padding(4.dp)
                                    )

                                }
                            }
                        }


                    }
                }
            }
        }
    }
}