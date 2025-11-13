package com.example.customcraft.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.customcraft.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Preview(showBackground = true)
@Composable
fun PreviewUserProfile() {
    UserProfile(navController = rememberNavController(), "m4Axo4BwJ3aYqhhckaBleHTV0Qb2")
}

@Composable
fun UserProfile(
    navController: NavController,
    userID: String,
    viewModel: UserProfileViewModel = viewModel()
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val userRating by viewModel.averageRating.collectAsState()
    val currentUser = Firebase.auth.currentUser?.uid.toString()

    LaunchedEffect(key1 = userID) {
        viewModel.fetchUserProfile(userID)
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
                Text(text = "Profile", fontSize = 26.sp)
            }
            HorizontalDivider()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            {
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly) {

                    Image(
                        painter = rememberAsyncImagePainter(model = userProfile.profileImage),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(100.dp).clip(CircleShape)
                    )
                    Text(
                        "Rating: ${userRating?.let { String.format("%.1f", it) } ?: "No ratings yet"}",
                        fontSize = 20.sp
                    )
                }
                Text(
                    text = userProfile.username,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(8.dp)
                )
                Text(text = userProfile.description)
                userProfile.tags?.let {
                    Box() {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            for (tag in userProfile.tags) {
                                Row() {
                                    Text(
                                        text = tag
                                    )
//                                    IconButton(
//                                        onClick = { /* Remove tag */ },
//                                        modifier = Modifier
//                                            .size(12.dp)
//                                    ) {
//                                        Icon(Icons.Default.Close, null)
//                                    }
                                }
                            }
                        }
//                        IconButton(
//                            onClick = { /* Display Add New Tag Dialog */  },
//                            modifier = Modifier
//                                .align(Alignment.CenterEnd)
//                                .size(16.dp)
//                        ) {
//                            Icon(Icons.Default.Add, null)
//                        }
                    }
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(.8f)
                ) {
                    items(userProfile.galleryImages.size) { index ->
                        val imageUrl = userProfile.galleryImages[index]
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(imageUrl)
                                    .error(R.drawable.cctemplogotransparent)
                                    .build(),


                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(2.dp)
                                .aspectRatio(1f)
                                .border(2.dp, Color.LightGray)
                        )

                    }
                }
                if (userID == currentUser) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = "Upload an image to your gallery", modifier = Modifier.padding(end = 8.dp))
                        IconButton(onClick = { navController.navigate("UploadImage") }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_file_upload_24),
                                contentDescription = "Upload an image to your gallery"
                            )
                        }
                    }
                }

                if (userID != currentUser) {
                Button(
                    onClick = { navController.navigate("commissions/${userID}")},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Request a Commission")
                }
                }
            }
        }
    }

}
