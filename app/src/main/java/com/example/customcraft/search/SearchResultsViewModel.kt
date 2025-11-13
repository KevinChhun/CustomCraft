package com.example.customcraft.search

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.customcraft.model.ArtistCardData
import com.example.customcraft.model.UserData
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchResultsViewModel: ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    val artistCardSearchResults: MutableState<List<ArtistCardData>> = mutableStateOf(emptyList())
    private val _state = MutableStateFlow<SearchResultsState>(SearchResultsState.Nothing)
    val state = _state.asStateFlow()

    fun search(query: String) {
        if (query.startsWith("#")) {
            searchByTag(query)
        } else {
            searchForUsers(query)
        }
    }

    private fun searchByTag(tagQuery: String) {
        val artistList = mutableListOf<ArtistCardData>()
        db.collection("users").whereEqualTo("artist", true).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val data = document.data
                    val artist = UserData(
                        id = data["id"] as String,
                        username = data["username"] as String,
                        description = data["description"] as String,
                        profileImgURL = data["profileImgURL"] as String,
                        spotlightImgURL = data["spotlightImgURL"] as String,
                        galleryImgURLs = data["galleryImgURLs"] as List<String>,
                        tags = data["tags"] as List<String>
                    )
                    if (artist.tags.contains(tagQuery)) {
                        val artistCard = createArtistCardDataFromUser(artist)
                        artistList.add(artistCard)
                    }
                }
                artistCardSearchResults.value = artistList
                Log.d("SEARCH RESULTS", artistList.toString())
            }
    }

    private fun searchForUsers(userName: String) {
        val artistList = mutableListOf<ArtistCardData>()
        db.collection("users").whereEqualTo("artist", true).whereEqualTo("username", userName).get()
            .addOnSuccessListener { result ->
                Log.d("FIRESTORE", "Success! Found ${result.size()} Records. (Search)")
                for (document in result) {
                    val data = document.data
                    val artist = UserData(
                        id = data["id"] as String,
                        username = data["username"] as String,
                        description = data["description"] as String,
                        profileImgURL = data["profileImgURL"] as String,
                        spotlightImgURL = data["spotlightImgURL"] as String,
                        galleryImgURLs = data["galleryImgURLs"] as List<String>,
                        tags = data["tags"] as List<String>
                    )
                    Log.d("SEARCH RESULTS", artist.toString())
                    val artistCard = createArtistCardDataFromUser(artist)
                    artistList.add(artistCard)
                    Log.d("SEARCH RESULTS", artistList.toString())
                }
                artistCardSearchResults.value = artistList
                Log.d("SEARCH RESULTS", artistList.toString())
            }
    }

    private fun createArtistCardDataFromUser(user: UserData) = ArtistCardData(
        artistData = user,
        spotlightImg = getImage(user.spotlightImgURL),
        profileImg = getImage(user.profileImgURL)
    )

    private fun getImage(url: String?): MutableState<Bitmap?> {
        val image: MutableState<Bitmap?> = mutableStateOf(null)

        url?.let {
            val storageRef = Firebase.storage.reference
            val imageRef = storageRef.child(url)
            imageRef.metadata.addOnSuccessListener { metadata ->
                val fileSize = metadata.sizeBytes.toInt()
                imageRef.getBytes(5 * 1024 * 1024).addOnSuccessListener { data ->
                    image.value = BitmapFactory.decodeByteArray(data, 0, fileSize)
                }
            }
        }
        return image
    }
}

sealed class SearchResultsState {
    object Nothing: SearchResultsState()
    object Loading: SearchResultsState()
    object Success: SearchResultsState()
    object Error: SearchResultsState()
}