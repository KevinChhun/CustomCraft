package com.example.customcraft.homePage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.customcraft.R
import com.example.customcraft.model.ArtistCardData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@Composable
fun Homepage(navController: NavController) {
    val viewModel: HomepageViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsState()
    var artistCardList: MutableState<List<ArtistCardData>> by remember { mutableStateOf(viewModel.artistCardList) }
    var artistCardSearchResults: MutableState<List<ArtistCardData>> by remember { mutableStateOf(viewModel.artistCardSearchResults) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentUser = Firebase.auth.currentUser?.uid.toString()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Image(
                    painter = painterResource(R.drawable.cctemplogotransparent),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Custom Craft",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp)
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label  = {
                        Text(
                            text = "Update Account Information",
                            fontSize = 16.sp
                        )},selected = false,
                    onClick = {navController.navigate("UpdateUser")
                    }
                )
                NavigationDrawerItem(
                    label  = {
                        Text(
                            text = "My Profile",
                            fontSize = 16.sp
                        )},selected = false,
                    onClick = {navController.navigate("profile/${currentUser}")
                    }
                )
                NavigationDrawerItem(
                    label  = {
                        Text(
                            text = "View Commissions (commissioner)",
                            fontSize = 16.sp
                        )},selected = false,
                    onClick = {navController.navigate("viewCommissions/${currentUser}")
                    }
                )
                NavigationDrawerItem(
                    label  = {
                        Text(
                            text = "My commissions (artist)",
                            fontSize = 16.sp
                        )},selected = false,
                    onClick = {navController.navigate("ViewCommissionsAsArtist/${currentUser}")
                    }
                )
                NavigationDrawerItem(
                    label = { Text(
                        text = "Log Out",
                        color = Color.Red,
                        fontSize = 16.sp

                    ) },
                    selected = false,
                    onClick = {
                        viewModel.signOut()
                        navController.navigate("signin")
                    }
                )
            }
        }
    ) {
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
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 48.dp, bottom = 16.dp)
                        ) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Filled.Menu, contentDescription = null,
                                modifier = Modifier
                                    .weight(1f))
                        }
                            var searchQuery by remember { mutableStateOf("") }
                            Box(
                                modifier = Modifier
                                    .wrapContentSize(Alignment.TopEnd)
                            ) {
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = { it ->
                                        searchQuery = it
                                    },
                                    label = { Text("Search") },
                                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                    singleLine = true,
                                    trailingIcon = {
                                        if (searchQuery.isNotEmpty()) {
                                            IconButton(onClick = { searchQuery = "" }) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    tint = MaterialTheme.colorScheme.onSurface,
                                                    contentDescription = "Clear search"
                                                )
                                            }
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                    keyboardActions = KeyboardActions(onSearch = {
                                        navController.navigate("search/${searchQuery}")
                                    })
                                )
                            }
                        IconButton(onClick = {
                            navController.navigate("channels")
                        }) {
                            Icon(
                                painterResource(R.drawable.baseline_chat_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                }
                Text(
                    text = "Artist Spotlight",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                if (uiState.value == HomepageState.Loading) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn {
                        items(artistCardList.value.size) { item ->
                            val username = artistCardList.value[item].artistData.username
                            val tags = artistCardList.value[item].artistData.tags
                            val description = artistCardList.value[item].artistData.description
                            val spotlightImage = artistCardList.value[item].spotlightImg.value
                            val profileImage = artistCardList.value[item].profileImg.value
                            val userUID = artistCardList.value[item].artistData.id

                            val spotlightPainter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(spotlightImage)
                                    .size(Size.ORIGINAL)
                                    .build()
                            )
                            val profilePainter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(profileImage)
                                    .size(Size.ORIGINAL)
                                    .build()
                            )

                            if (spotlightPainter.state is AsyncImagePainter.State.Success && profilePainter.state is AsyncImagePainter.State.Success) {
                                Card(
                                    onClick = { navController.navigate("profile/${userUID}") },
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                        .fillMaxWidth()
                                        .height(160.dp)

                                ) {
                                    Box {
                                        Image(
                                            painter = spotlightPainter,
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        )
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    Brush.verticalGradient(
                                                        colors = listOf(
                                                            Color.Transparent,
                                                            Color.Black
                                                        ),
                                                        startY = 300f
                                                    )
                                                )
                                        )
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .padding(8.dp)
                                            ) {

                                                Image(
                                                    painter = profilePainter,
                                                    contentDescription = null,
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier
                                                        .size(100.dp)
                                                        .clip(CircleShape)
                                                        .border(2.dp, Color.Gray, CircleShape)
                                                )
                                                Column {
                                                    Column {
                                                        if (username != null) {
                                                            Text(
                                                                text = username,
                                                                fontWeight = FontWeight.Bold,
                                                                color = Color.White
                                                            )
                                                        }
                                                        HorizontalDivider()
                                                        if (tags.size >= 2) {
                                                            Text(
                                                                text = tags[0] + " " + tags[1],
                                                                color = Color.Blue
                                                            )
                                                        } else if (tags.size == 1) {
                                                            Text(
                                                                text = tags[0],
                                                                color = Color.Blue

                                                            )
                                                        }

                                                    }
                                                }
                                            }
                                            Column(
                                                verticalArrangement = Arrangement.Bottom,
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    .fillMaxSize()
                                            ) {
                                                description?.let {
                                                    Text(
                                                        text = description,
                                                        color = Color.White,
                                                        maxLines = 2,
                                                        overflow = TextOverflow.Ellipsis
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
            }
        }
    }
}
