package com.example.customcraft.chat


import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.customcraft.model.Message
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(): ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val message = _messages.asStateFlow()
    private val db = FirebaseFirestore.getInstance()

    fun sendMessage(channelID: String, messageText: String) {
        val message = Message(
            id = UUID.randomUUID().toString(),
            senderId = Firebase.auth.currentUser?.uid ?: "",
            senderName = Firebase.auth.currentUser?.displayName ?: "",
            senderImage = null,
            message = messageText,
            imageUrl = null
        )
        val parentDocRef = db.collection("channels").document(channelID)
        parentDocRef.collection("messages").document(message.id).set(message)
    }

    fun listenForMessages(channelID: String) {
        db.collection("channels").document(channelID).collection("messages")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("Firestore", "Listen failed.", e)
                    return@addSnapshotListener
                }
                val list = mutableListOf<Message>()
                snapshot?.forEach { data ->
                    val message = data.toObject(Message::class.java)
                    message?.let { list.add(it) }
                }
                _messages.value = list
            }
    }
}
//            .addValueEventListener(object: ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val list = mutableListOf<Message>()
//                    snapshot.children.forEach { data ->
//                        val message = data.getValue(Message::class.java)
//                        message?.let {
//                            list.add(it)
//                        }
//                    }
//                    _messages.value = list
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//
//            })
