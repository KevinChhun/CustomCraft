package com.example.customcraft.viewCommissions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.customcraft.model.Commission
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class commissionDetailsViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    fun getCommission(commissionID: String) = liveData {
        try {
            val documentSnapshot = db.collection("commissions")
                .document(commissionID)
                .get()
                .await()

            if (documentSnapshot.exists()) {
                val commission = documentSnapshot.toObject(Commission::class.java)
                commission?.let {
                    emit(it)
                } ?: run {
                    println("Document exists but failed to map to Commission object.")
                    emit(null)
                }
            } else {
                println("Document does not exist.")
                emit(null)
            }
        } catch (e: Exception) {
            println("Error fetching document: ${e.message}")
            emit(null)
        }
    }
}
