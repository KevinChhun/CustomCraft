package com.example.customcraft.updateUserInfo

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

@Composable
fun UpdateShowcaseImage(navController: NavHostController) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.fillMaxWidth().wrapContentHeight(align = Alignment.Bottom)
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
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
                    text = "Change Spotlight Image",
                    fontSize = 26.sp
                )
            }
            HorizontalDivider()
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(72.dp)
        ) {
            imageUri?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(500.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { imagePicker.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Image")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        imageUri?.let { uri ->
                            scope.launch {
                                val result = UpdateShowcaseImage(uri)
                                if (result) {
                                    snackbarHostState.showSnackbar("Image uploaded successfully!")
                                } else {
                                    snackbarHostState.showSnackbar("Failed to upload image!")
                                }
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Confirm Image Selection")
                }
            }
        }
    }
}

private suspend fun UpdateShowcaseImage(uri: Uri): Boolean {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()
    val userDoc = db.collection("users").document(userId.toString()).get().await()
    val currentProfileImgURL = userDoc.getString("spotlightImgURL")
    if (currentProfileImgURL != null && currentProfileImgURL != "images/DefaultProfilePic.png") {
        val oldImageRef = storage.reference.child(currentProfileImgURL)
        oldImageRef.delete().await()
    }

    val imageUniqueID = UUID.randomUUID()
    val newImageRef = storage.reference.child("images/${userId}/spotlightImg/${imageUniqueID}.png")
    return try {
        val uploadTask = newImageRef.putFile(uri).await()
        db.collection("users").document(userId.toString()).update(
            "spotlightImgURL", "images/${userId}/spotlightImg/${imageUniqueID}.png"
        )
        true
    } catch (e: Exception) {
        false
    }
}
