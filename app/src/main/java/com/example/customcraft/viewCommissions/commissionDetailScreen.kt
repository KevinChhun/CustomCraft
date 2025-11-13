package com.example.customcraft.viewCommissions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.customcraft.model.Channel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.UUID


@Composable

fun CommissionDetailsScreen(
    commissionID: String,
    navController: NavController,
    viewModel: commissionDetailsViewModel = viewModel()
) {
    val storage = FirebaseStorage.getInstance()
    val db = FirebaseFirestore.getInstance()
    val currentUser = Firebase.auth.currentUser?.uid.toString()
    val commission = viewModel.getCommission(commissionID).observeAsState()
    val profilePicUrl = remember { mutableStateOf("") }
    val isChannelSaved = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val dialogMessage = remember { mutableStateOf("") }
    val dialogAction: MutableState<() -> Unit> = remember { mutableStateOf({}) }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 48.dp)) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "Commission",
                    fontSize = 26.sp,
                    modifier = Modifier.padding(start = 2.dp)
                )

            }
            HorizontalDivider()

            commission.value?.let { commissionData ->
                if (!commissionData.commissionDenied) {


                    val timestamp = commissionData.createdAt
                    val date = timestamp?.toDate()
                    val formatter = SimpleDateFormat("MM-dd-yyyy")
                    val formattedDate = formatter.format(date)

                    if (currentUser == commissionData.commissionerID) {
                        val storageRef = storage.reference.child(commissionData.artistProfilePic)
                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            profilePicUrl.value = uri.toString()
                        }
                    } else {
                        val storageRef =
                            storage.reference.child(commissionData.commissionerProfilePic)
                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            profilePicUrl.value = uri.toString()
                        }
                    }

                    if (profilePicUrl.value.isNotEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(model = profilePicUrl.value),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(400.dp)
                                .clip(CircleShape)
                        )
                    }

                    if (currentUser == commissionData.artistID) {
                        Text(
                            text = "Commissioner Name: ${commissionData.commissionerName}",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(bottom = 8.dp, top = 12.dp)
                        )
                    }
                    Text(
                        text = "Commission: ${commissionData.commission}",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    if (currentUser == commissionData.commissionerID) {
                        Text(
                            text = "Artist Name: ${commissionData.artistName}",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    Text(
                        text = "The cost: \$${commissionData.money}",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    if (currentUser == commissionData.commissionerID) {
                        Text(
                            modifier = Modifier.padding(bottom = 8.dp),
                            fontSize = 20.sp,
                            text =
                            "Status: ${
                                when {
                                    commissionData.commissionComplete -> "Completed"
                                    commissionData.commissionInProgress -> "In Progress"
                                    commissionData.commissionDenied -> "Commission Denied"
                                    else -> "Pending acceptance"
                                }
                            }"
                        )
                    }
                    if (currentUser == commissionData.artistID) {
                        Text(
                            text = "Commission Declined: ${commissionData.commissionDenied}",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Commission Accepted: ${commissionData.commissionAccepted}",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Commission In Progress: ${commissionData.commissionInProgress}",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "Is Complete: ${commissionData.commissionComplete}",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    Text(
                        text = "Created At: $formattedDate",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = {
                                    val channelId =
                                        "${commissionData.artistID}-${commissionData.commissionerID}"
                                    val existingChannelRef = db.collection("channels")
                                        .whereEqualTo(
                                            "members.${commissionData.artistID}",
                                            commissionData.artistName
                                        )
                                        .whereEqualTo(
                                            "members.${commissionData.commissionerID}",
                                            commissionData.commissionerName
                                        )

                                    existingChannelRef.get()
                                        .addOnSuccessListener { querySnapshot ->
                                            if (querySnapshot.isEmpty) {
                                                val channel = Channel(
                                                    id = UUID.randomUUID().toString(),
                                                    members = mapOf(
                                                        commissionData.artistID to commissionData.artistName,
                                                        commissionData.commissionerID to commissionData.commissionerName
                                                    )
                                                )
                                                db.collection("channels").document(channel.id)
                                                    .set(channel)
                                                    .addOnSuccessListener {
                                                        isChannelSaved.value = true
                                                    }
                                                    .addOnFailureListener {
                                                    }
                                            } else {
                                            }
                                        }
                                        .addOnFailureListener {
                                        }
                                    isChannelSaved.value = true
                                },
                                modifier = Modifier
                                    .padding(top = 16.dp, bottom = 16.dp)

                            ) {
                                Text(text = "Chat with the user")
                            }
                        }
                        if (currentUser == commissionData.commissionerID && commissionData.commissionComplete) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {

                                Button(
                                    onClick = { navController.navigate("paymentScreen") }
                                ) {
                                    Text("Pay the artist")
                                }
                                Button(
                                    onClick = { navController.navigate("ratings/${commissionData.artistID}") },
                                ) {
                                    Text(text = "Rate the Artist")
                                }
                            }
                        }
                        if (currentUser == commissionData.artistID) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
                                    onClick = {
                                        dialogMessage.value =
                                            "Are you sure you want to accept this commission?"
                                        dialogAction.value = {
                                            db.collection("commissions").document(commissionID)
                                                .update("commissionAccepted", true)
                                                .addOnSuccessListener { /* Handle success */ }
                                                .addOnFailureListener { /* Handle failure */ }
                                        }
                                        showDialog.value = true
                                    }
                                ) { Text("Accept commission!") }

                                Button(
                                    onClick = {
                                        dialogMessage.value =
                                            "Are you sure you want to mark this commission as in progress?"
                                        dialogAction.value = {
                                            db.collection("commissions").document(commissionID)
                                                .update("commissionInProgress", true)
                                                .addOnSuccessListener { /* Handle success */ }
                                                .addOnFailureListener { /* Handle failure */ }
                                        }
                                        showDialog.value = true
                                    }
                                ) { Text("Commission in progress?") }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
                                    onClick = {
                                        dialogMessage.value =
                                            "Are you sure you want to mark this commission as completed?"
                                        dialogAction.value = {
                                            db.collection("commissions").document(commissionID)
                                                .update("commissionComplete", true)
                                                .addOnSuccessListener { /* Handle success */ }
                                                .addOnFailureListener { /* Handle failure */ }
                                        }
                                        showDialog.value = true
                                    }
                                ) { Text("Complete Commission") }

                                Button(
                                    onClick = {
                                        dialogMessage.value =
                                            "Are you sure you want to Deny this commission?"
                                        dialogAction.value = {
                                            db.collection("commissions").document(commissionID)
                                                .update("commissionDenied", true)
                                                .addOnSuccessListener { /* Handle success */ }
                                                .addOnFailureListener { /* Handle failure */ }
                                        }
                                        showDialog.value = true
                                    }
                                ) { Text("Decline commission") }
                            }
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    db.collection("commissions").document(commissionID).delete()
                                }) {
                                Text("Delete the denied commission.")
                            }
                        }
                    }

                }
            }
        }

        if (isChannelSaved.value) {
            LaunchedEffect(Unit) {
                delay(500)
                navController.navigate("channels")
            }
        }

        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text("Confirmation") },
                text = { Text(dialogMessage.value) },
                confirmButton = {
                    Button(
                        onClick = {
                            dialogAction.value()
                            showDialog.value = false
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog.value = false }
                    ) {
                        Text("No")
                    }
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(35.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,

            ) {

        }

    }
}