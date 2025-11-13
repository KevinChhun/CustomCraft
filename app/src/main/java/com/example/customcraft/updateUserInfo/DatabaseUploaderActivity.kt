package com.example.customcraft.updateUserInfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.customcraft.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

@Preview(showBackground = true)
@Composable
fun UpdateUserDataPreview() {
    val navController = rememberNavController()
    UpdateUserData(navController = navController)
}
@Composable
fun UpdateUserData(navController: NavHostController) {
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()
    val id = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    var username by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var profileImgURL by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var artist by remember { mutableStateOf(false) }

    // loading and initializing all of the data from the database (I used this to autofill the fields so the user has an easier time updating)
    LaunchedEffect(id) {
        if (id.isNotEmpty()) {
            db.collection("users").document(id).get().addOnSuccessListener { document ->
                if (document != null) {
                    username = document.getString("username") ?: ""
                    description = document.getString("description") ?: ""
                    profileImgURL = document.getString("profileImgURL") ?: ""
                    artist = document.getBoolean("artist") ?: false
                    tags = (document.get("tags") as? List<String>)?.joinToString(", ") ?: ""
                    val storageRef = storage.reference.child(profileImgURL)
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        profileImgURL = uri.toString()
                    }
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
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
                text = "Update Account Data",
                fontSize = 26.sp
            )
        }
        HorizontalDivider()
    }

    Column(modifier = Modifier.padding(72.dp)) {
        Image(
            painter = rememberAsyncImagePainter(model = profileImgURL),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(400.dp).clip(CircleShape)
        )
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = tags,
            onValueChange = { tags = it },
            label = { Text("Update tags (separate by commas)") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly) {
            Text("Are you an artist?")
            Switch(
                checked = artist,
                onCheckedChange = { artist = it }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { navController.navigate("UpdateProfilePicture") },
                modifier = Modifier.weight(1f)
            ) {
                Text("Change Profile Pic")
            }
            Button(
                onClick = { navController.navigate("UpdateShowcaseImage") },
                modifier = Modifier.weight(1f)
            ) {
                Text("Change Spotlight Pic")
            }
        }
        Row {
            Button(
                onClick = {
                    val tagList = tags.split(",").map {
                        val trimmedTag = it.trim()
                        if (trimmedTag.startsWith("#")) trimmedTag else "#$trimmedTag"
                    }
                    val user = UserData(
                        id = id,
                        username = username,
                        description = description,
                        profileImgURL = profileImgURL,
                        tags = tagList,
                        artist = artist
                    )

                    db.collection("users")
                        .document(user.id)
                        .apply {
                            if (user.description.isNotEmpty()) {
                                update("description", user.description)
                            }
                            if (user.username.isNotEmpty()) {
                                update("username", user.username)
                            }
                            update("artist", user.artist)
                            if (user.tags.isNotEmpty()) {
                                update("tags", user.tags)
                            }
                        }
                },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text(text = "Upload User Data")
            }
        }
    }
}