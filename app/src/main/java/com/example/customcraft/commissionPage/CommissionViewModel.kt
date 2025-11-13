package com.example.customcraft.commissionPage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customcraft.model.Commission
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CommissionViewModel @Inject constructor() : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val commissionerID = Firebase.auth.currentUser?.uid.orEmpty()
    private val _commissions = MutableStateFlow<List<Commission>>(emptyList())
    val commissions: StateFlow<List<Commission>> = _commissions

    init {
        loadCommissions()

    }

    private fun loadCommissions() {
        viewModelScope.launch {
            try {
                Log.d("LoadCommissions", "Loading commissions for user: $commissionerID")

                val documents = db.collection("commissions")
                    .whereEqualTo(
                        "commissionerID",
                        commissionerID
                    )
                    .get()
                    .await()

                Log.d("LoadCommissions", "Found ${documents.size()} commissions")

                val commissionList = documents.mapNotNull { document ->
                    Log.d("LoadCommissions", "Document ID: ${document.id}, Data: ${document.data}")
                    document.toObject(Commission::class.java)
                }
                Log.d("LoadCommissions", "Loaded commissions: $commissionList")

                _commissions.value = commissionList
            } catch (e: Exception) {
                Log.e("LoadCommissions", "Error loading commissions", e)
            }
        }
    }


    fun uploadCommission(commission: String, money: Double, artistID: String) {
        viewModelScope.launch {

            val commissionerSnapshot = db.collection("users").document(commissionerID).get().await()
            val commissionerName = commissionerSnapshot.getString("username")
            val commissionerProfilePic = commissionerSnapshot.getString("profileImgURL")
            val artistSnapshot = db.collection("users").document(artistID).get().await()
            val artistName = artistSnapshot.getString("username")
            val artistProfilePic = artistSnapshot.getString("profileImgURL")
            val newCommission = Commission(
                commissionID = UUID.randomUUID().toString(),
                commissionerName = commissionerName ?: "Unknown",
                commissionerProfilePic = commissionerProfilePic ?: "images/DefaultProfilePic.png",
                artistID = artistID,
                artistName = artistName ?: "Unknown",
                artistProfilePic = artistProfilePic ?: "images/DefaultProfilePic.png",
                commissionerID = commissionerID,
                commission = commission,
                money = money,
                commissionComplete = false,
                commissionDenied = false,
                commissionInProgress = false,
                commissionAccepted = false,
                createdAt = Timestamp.now(),
                updatedAt = Timestamp.now()
            )
            db.collection("commissions")
                .document("${newCommission.commissionID}")
                .set(newCommission)
                .await()
            loadCommissions()
        }
    }
}
