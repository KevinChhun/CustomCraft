package com.example.customcraft.homePage

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
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.lang.Exception
import javax.inject.Inject
import com.google.firebase.auth.auth

@HiltViewModel
class HomepageViewModel @Inject constructor() : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    val artistCardList: MutableState<List<ArtistCardData>> = mutableStateOf(emptyList())
    val artistCardSearchResults: MutableState<List<ArtistCardData>> = mutableStateOf(emptyList())

    private val _state = MutableStateFlow<HomepageState>(HomepageState.Nothing)
    val state = _state.asStateFlow()

    init {
        getShowcaseUsers()
    }

    private fun getShowcaseUsers() {
        _state.value = HomepageState.Loading
        val artistCards = mutableListOf<ArtistCardData>()
        db.collection("users")
            .whereEqualTo("onShowcase", true)
            .get()
            .addOnSuccessListener { result ->
                createArtistCardsAndAddToList(result, artistCards)
                _state.value = HomepageState.Success
            }
            .addOnFailureListener { e ->
                logDatabaseReadFailure(e)
                _state.value = HomepageState.Error
            }
    }


    private fun logDatabaseReadFailure(e: Exception) {
        Log.w("FIRESTORE", "FAILURE!", e)
    }

    private fun createArtistCardsAndAddToList(
        result: QuerySnapshot,
        artistCards: MutableList<ArtistCardData>
    ) {
        for (document in result) {
            val user = createUserFromData(document.data)
            val artistCard = createArtistCardDataFromUser(user)
            artistCards.add(artistCard)
        }
        artistCardList.value = artistCards
    }

    private fun createArtistCardDataFromUser(user: UserData) = ArtistCardData(
        artistData = user,
        spotlightImg = getImage(user.spotlightImgURL),
        profileImg = getImage(user.profileImgURL)
    )

    private fun createUserFromData(data: Map<String, Any>) = UserData(
        id = data["id"] as String,
        username = data["username"] as String,
        description = data["description"] as String,
        profileImgURL = data["profileImgURL"] as String,
        spotlightImgURL = data["spotlightImgURL"] as String,
        galleryImgURLs = data["galleryImgURLs"] as List<String>,
        tags = data["tags"] as List<String>,
        artist = data["artist"] as Boolean,
        onShowcase = data["onShowcase"] as Boolean
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

    fun signOut() {
        Firebase.auth.signOut()
    }

    fun searchForUsers(userName: String) {
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
}

sealed class HomepageState {
    object Nothing : HomepageState()
    object Loading : HomepageState()
    object Success : HomepageState()
    object Error : HomepageState()

}