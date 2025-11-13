package com.example.customcraft.viewCommissions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customcraft.model.Commission
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class artistCommissionViewModel @Inject constructor() : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val artistID = Firebase.auth.currentUser?.uid.orEmpty()
    private val _commissions = MutableStateFlow<List<Commission>>(emptyList())
    val commissions: StateFlow<List<Commission>> = _commissions

    init {
        loadCommissionsAsArtist()
    }

    private fun loadCommissionsAsArtist() {
        viewModelScope.launch {
            try {
                Log.d("LoadCommissions", "Loading commissions for user: $artistID")

                val documents = db.collection("commissions")
                    .whereEqualTo(
                        "artistID",
                        artistID
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

}

