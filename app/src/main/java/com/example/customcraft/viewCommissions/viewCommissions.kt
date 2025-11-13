package com.example.customcraft.viewCommissions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.customcraft.commissionPage.CommissionViewModel
import com.example.customcraft.model.Commission
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewCommissionsScreen(
    commissionerID: String,
    viewModel: CommissionViewModel,
    navController: NavController
) {
    val commissions = viewModel.commissions.collectAsState().value
        .filter { it.commissionerID == commissionerID }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                    contentDescription = "back"
                )
            }
            Text(
                text = "View Commissions",
                fontSize = 26.sp
            )
        }
        HorizontalDivider()
        Row() {
            LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
                items(commissions) { commission ->
                    CommissionItem(commission, navController)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (commissions.isEmpty()) {
            Text("You have not made or been sent any commissions")
        }

    }
}


@Composable
fun CommissionItem(commission: Commission, navController: NavController) {
    ListItem(
        modifier = Modifier.clickable {
            navController.navigate("commissionDetailsScreen/${(commission.commissionID)}")
        },
        headlineContent = {
            Text("Artist Name: ${commission.artistName}")
        },
        supportingContent = {
            Text("Description of commission: ${commission.commission}")
            Text("Price: \$${commission.money}")
            Text(
                "Status: ${
                    when {
                        commission.commissionComplete -> "Completed"
                        commission.commissionInProgress -> "In Progress"
                        else -> "Pending acceptance"
                    }
                }"
            )
        }
    )
}
