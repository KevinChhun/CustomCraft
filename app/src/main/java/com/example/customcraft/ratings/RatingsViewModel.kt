package com.example.customcraft.ratings

import com.example.customcraft.model.Rating
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
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
class RatingsViewModel @Inject constructor() : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _ratings = MutableStateFlow<List<Rating>>(emptyList())
    val ratings: StateFlow<List<Rating>> = _ratings

    private val _averageRating = MutableStateFlow<Double?>(null)
    val averageRating: StateFlow<Double?> = _averageRating

    private fun loadRatings(artistID: String) {
        viewModelScope.launch {
            try {
                val documents = db.collection("ratings")
                    .whereEqualTo("id", artistID)
                    .get()
                    .await()

                val ratingsList = documents.mapNotNull { document ->
                    document.toObject(Rating::class.java)
                }

                _ratings.value = ratingsList
                calculateRatingAvg(ratingsList)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun calculateRatingAvg(ratingsList: List<Rating>) {
        if(ratingsList.isNotEmpty()) {
            val average = ratingsList.map { it.ratingNumber }.average()
            _averageRating.value = average
        } else {
            _averageRating.value = null
        }
    }

    fun uploadRating(ratingNumber: Double, artistID: String) {
        viewModelScope.launch {
            try {
                val newRating = Rating(
                    artistID = artistID,
                    ratingNumber = ratingNumber,
                    isComplete = false,
                    ratingInProgress = false
                )

                db.collection("ratings")
                    .document(artistID)
                    .collection("ratings")
                    .document()
                    .set(newRating)
                    .await()


                loadRatings(artistID)

            } catch (e: Exception) {
                println("Failed to upload rating: ${e.message}")
                e.printStackTrace()
            }
        }
    }

}