package com.example.customcraft.ratings

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

private val db = FirebaseFirestore.getInstance()

private val storage = FirebaseStorage.getInstance()

fun calculateRatingAvg(userID:String, onSuccess: (Double?) -> Unit){
    db.collection("users").document(userID).get().addOnSuccessListener { document ->
        val ratings = document.get("ratings") as? List<Double> ?: emptyList()

        if(ratings.isNotEmpty()) {
            val average = ratings.average()
            onSuccess(average)
        } else {
            onSuccess(null)
        }
    } .addOnFailureListener() { exception ->
        exception.printStackTrace()
        onSuccess(null)
    }
}
