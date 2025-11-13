package com.example.customcraft.chat

import androidx.lifecycle.ViewModel
import com.example.customcraft.model.Channel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChannelsViewModel @Inject constructor(): ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val currentUser = Firebase.auth.currentUser?.uid.toString()
    private val _channels = MutableStateFlow<List<Channel>>(emptyList())
    val channels = _channels.asStateFlow()

    init {
        getChannels()
    }

    private fun getChannels() {
        db.collection("channels").get()
            .addOnSuccessListener { documents ->
                val list = mutableListOf<Channel>()
                for (document in documents) {
                    val currentChannel = document.toObject(Channel::class.java)

                    if (currentChannel.members.keys.contains(Firebase.auth.currentUser?.uid)) {
                        list.add(currentChannel)
                    }
                }
                _channels.value = list
            }
    }

    fun getChannelName(channel: Channel): String {
        var channelName = ""
        for (member in channel.members) {
            if (member.key != currentUser)
                channelName = member.value
        }
        return channelName
    }

    fun createTestChannel() {
        val channel = Channel(
            id = UUID.randomUUID().toString(),
            members = mapOf("7TsI431iBEgzoAiy92FSUAuRxag1" to "test-user",
                "Bde8q5xqWnXbHflo6G5iKDL2Fem2" to "PatrickAdairDesigns")
        )
        db.collection("channels").document(channel.id).set(channel).addOnSuccessListener {
            getChannels()
        }
    }


}