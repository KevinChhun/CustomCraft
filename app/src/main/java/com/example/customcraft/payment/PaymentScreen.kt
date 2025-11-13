package com.example.customcraft.payment

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun PaymentScreen(navController: NavController) {
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
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
                        text = "Shipping & Payment",
                        fontSize = 26.sp
                    )
                }
                HorizontalDivider()
            Text(
                text = "Shipping Address",
                fontSize = 26.sp,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            var fullName by remember { mutableStateOf("") }
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text(text = "Full Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            var phoneNumber by remember { mutableStateOf("") }
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text(text = "Phone Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            var shippingAddress1 by remember { mutableStateOf("") }
            OutlinedTextField(
                value = shippingAddress1,
                onValueChange = { shippingAddress1 = it },
                label = { Text(text = "Address Line 1") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            var shippingAddress2 by remember { mutableStateOf("") }
            OutlinedTextField(
                value = shippingAddress2,
                onValueChange = { shippingAddress2 = it },
                label = { Text(text = "Address Line 2") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                var shippingCity by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = shippingCity,
                    onValueChange = { shippingCity = it },
                    label = { Text(text = "City") },
                    modifier = Modifier
                        .width(200.dp)
                )
                var shippingState by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = shippingState,
                    onValueChange = { shippingState = it },
                    label = { Text(text = "State") },
                    modifier = Modifier
                        .weight(.25f)
                )
                var shippingZip by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = shippingZip,
                    onValueChange = { shippingZip = it },
                    label = { Text(text = "Zipcode") },
                    modifier = Modifier
                        .weight(.3f)
                )
            }
            Text(
                text = "Debit/Credit Card",
                fontSize = 26.sp,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            var ccNumber by remember { mutableStateOf("") }
            OutlinedTextField(
                value = ccNumber,
                onValueChange = { ccNumber = it },
                label = { Text(text = "Card Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            var ccName by remember { mutableStateOf("") }
            OutlinedTextField(
                value = ccName,
                onValueChange = { ccName = it },
                label = { Text(text = "Name on Card") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                var ccMonth by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = ccMonth,
                    onValueChange = { ccMonth = it },
                    label = { Text(text = "MM") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .width(75.dp)
                )
                var ccYear by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = ccYear,
                    onValueChange = { ccYear = it },
                    label = { Text(text = "YYYY") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .width(150.dp)
                )
            }
            var ccCVV by remember { mutableStateOf("") }
            OutlinedTextField(
                value = ccCVV,
                onValueChange = { ccCVV = it },
                label = { Text(text = "CVV/CVC") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .width(150.dp)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Button(
                onClick = {
                    if (!fullName.isBlank() && !phoneNumber.isBlank() && !shippingAddress1.isBlank() && !ccName.isBlank() && !ccNumber.isBlank() && !ccCVV.isBlank()) {
                        Toast.makeText(context,
                            "Commission has been sent!",
                            Toast.LENGTH_SHORT)
                            .show()
                        navController.navigate("home")
                    } else {
                        Toast.makeText(
                            context,
                            "Please enter data in all fields",
                            Toast.LENGTH_LONG)
                            .show()
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text = "Purchase")
            }
        }
    }
}