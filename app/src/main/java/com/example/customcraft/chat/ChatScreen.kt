package com.example.customcraft.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.customcraft.model.Message
import com.example.customcraft.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


@Composable
fun ChatScreen(
    navController: NavController,
    channelId: String,
    channelName: String) {
    Scaffold (
        containerColor = Color.White
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "back")
                }
                Text(
                    text = channelName,
                    fontSize = 26.sp
                )

            }
            HorizontalDivider()
            val viewModel: ChatViewModel = hiltViewModel()
            LaunchedEffect(key1 = true) {
                viewModel.listenForMessages(channelId)
            }

            val messages = viewModel.message.collectAsState()
            ChatMessages(
                messages = messages.value,
                onSendMessage = { message ->
                    viewModel.sendMessage(channelId, message)
                })
        }

    }
}

@Composable
fun ChatMessages(
    messages: List<Message>,
    onSendMessage: (String) -> Unit
) {
    val msg = remember { mutableStateOf("") }
    val hideKeyboardController = LocalSoftwareKeyboardController.current
    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        LazyColumn {
            items(messages) { message ->
                ChatBubble(message = message)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.LightGray)
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically

        ) {
            IconButton(onClick = {
                msg.value = ""
            }) {
                Image(painter = painterResource(id = R.drawable.attach), contentDescription = "attach",
                    modifier = Modifier
                        .weight(1f))
            }
            TextField(
                value = msg.value,
                onValueChange = { msg.value = it },
                placeholder = {
                    Text(
                        text = "Type a message")
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        hideKeyboardController?.hide()
                    }
                ),
                maxLines = 3,
                modifier = Modifier
                    .weight(4f)
                )
            IconButton(
                onClick = {
                    onSendMessage(msg.value)
                    msg.value = ""
                }) {
                Image(painter = painterResource(id = R.drawable.send),
                    contentDescription = "send",
                    modifier = Modifier
                        .weight(1f))
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    val isCurrentUser = message.senderId == Firebase.auth.currentUser?.uid
    val bubbleColor = if (isCurrentUser) {
        Color.Blue
    } else {
        Color.LightGray
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp,
                horizontal = 8.dp)
    ) {
        val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
        Row(
            modifier = Modifier
                .padding(8.dp)
                .align(alignment),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(!isCurrentUser) {
                Image(
                    painter = painterResource(id = R.drawable.cctemplogotransparent),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = message.message.trim(),
                color = if (isCurrentUser) {
                    Color.White
                } else {
                    Color.Black
                },
                modifier = Modifier
                    .background(color = bubbleColor, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            )
        }
    }
}
