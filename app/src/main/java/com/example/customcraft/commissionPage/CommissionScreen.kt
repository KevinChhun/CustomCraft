package com.example.customcraft.commissionPage

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun CommissionScreen(navController: NavController,userID: String) {
    val viewModel = hiltViewModel<CommissionViewModel>()
    val commission = viewModel.commissions.collectAsState()
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }
                )
                {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "back"
                    )
                }
                Text(
                    text = "Request a Commission",
                    fontSize = 26.sp
                )
            }
            HorizontalDivider()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var commission by remember {
                    mutableStateOf("")

                }
                TextField(
                    value = commission,
                    onValueChange = { commission = it },
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Left
                    ),
                    label = {
                        Text(text = "Write your Commission")
                    },
                    supportingText = {
                        Text(text = "*required*")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp),
                    maxLines = Int.MAX_VALUE
                )

                var money by remember {
                    mutableStateOf("")

                }
                TextField(
                    value = money,
                    onValueChange = {
                        if(it.matches(Regex("^\\d+"))){
                            money = it }
                                    },
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Left
                    ),
                    label = {
                        Text(text = "Payment in USD ($)")
                    },
                    prefix = {
                        Text(text = "$")
                    },
                    supportingText = {
                        Text(text = "*required*")
                    },
                    modifier = Modifier
                        .padding(vertical = 50.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    )

                )
                Button(
                    onClick = {
                        if (!money.isBlank()){
                            viewModel.uploadCommission(commission,money.toDouble(),userID)
                            Toast.makeText(context, "Commission has been sent!", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(context, "Please enter a number", Toast.LENGTH_SHORT).show()
                            }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp)
                ) {
                    Text(
                        text = "Send"
                    )
                }
            }
        }
    }
}
