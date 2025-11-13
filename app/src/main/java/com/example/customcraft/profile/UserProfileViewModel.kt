package com.example.customcraft.profile


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customcraft.model.Rating
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UserProfileData(
    val username: String = "",
    val description: String = "",
    val galleryImages: List<String> = emptyList(),
    val profileImage: String = "",
    val tags: List<String> = emptyList()
)

class UserProfileViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val _userProfile = MutableStateFlow(UserProfileData())
    val userProfile: StateFlow<UserProfileData> = _userProfile

    private val _averageRating = MutableStateFlow<Double?>(null)
    val averageRating: StateFlow<Double?> = _averageRating
    private val _ratings = MutableStateFlow<List<Rating>>(emptyList())
    val ratings: StateFlow<List<Rating>> = _ratings


    fun fetchUserProfile(userID: String) {
        viewModelScope.launch {
            db.collection("users").document(userID).get().addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val username = document.getString("username") ?: ""
                    val description = document.getString("description") ?: ""
                    val galleryImages = document.get("galleryImgURLs") as? List<String> ?: emptyList()
                    val profileImage = document.getString("profileImgURL") ?: ""
                    val tags = document.get("tags") as? List<String> ?: emptyList()

                    if (profileImage.isNotEmpty()) {
                        val profileImageRef = storage.reference.child(profileImage)
                        profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                            val galleryImageUrls = mutableListOf<String>()
                            galleryImages.forEach { img ->
                                val imageRef = storage.reference.child(img)
                                imageRef.downloadUrl.addOnSuccessListener { imageUri ->
                                    galleryImageUrls.add(imageUri.toString())
                                    if (galleryImageUrls.size == galleryImages.size) {
                                        _userProfile.value = UserProfileData(username, description, galleryImageUrls, uri.toString(), tags)
                                    }
                                }
                            }
                        }
                    } else {
                        _userProfile.value = UserProfileData(username, description, galleryImages, profileImage, tags)
                    }
                }
            }
            val ratingList = mutableListOf<Rating>()
            db.collection("ratings").document(userID).collection("ratings").get().addOnSuccessListener { result ->
            for (document in result)
            {
                val data = document.data
                val rating = Rating(artistID = data["artistID"] as String,
                    ratingNumber = data["ratingNumber"] as Double)
                ratingList.add(rating)
            }
                _ratings.value = ratingList
                calculateRatingAvg(ratingList)
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
}
