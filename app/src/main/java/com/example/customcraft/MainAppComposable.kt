package com.example.customcraft

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.customcraft.chat.ChannelsScreen
import com.example.customcraft.chat.ChatScreen
import com.example.customcraft.commissionPage.CommissionScreen
import com.example.customcraft.commissionPage.CommissionViewModel
import com.example.customcraft.updateUserInfo.UploadImageScreen
import com.example.customcraft.updateUserInfo.UpdateUserData
import com.example.customcraft.homePage.Homepage
import com.example.customcraft.login.passwordreset.PasswordResetScreen
import com.example.customcraft.login.signin.SignInScreen
import com.example.customcraft.login.signup.SignUpScreen
import com.example.customcraft.payment.PaymentScreen
import com.example.customcraft.profile.UserProfile
import com.example.customcraft.ratings.RatingsScreen
import com.example.customcraft.updateUserInfo.UpdateProfilePicture
import com.example.customcraft.updateUserInfo.UpdateShowcaseImage
import com.example.customcraft.search.SearchResults
import com.example.customcraft.viewCommissions.ViewCommissionsAsArtist
import com.example.customcraft.viewCommissions.ViewCommissionsScreen
import com.example.customcraft.viewCommissions.artistCommissionViewModel
import com.example.customcraft.viewCommissions.CommissionDetailsScreen
import com.example.customcraft.viewCommissions.commissionDetailsViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun MainApp() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val navController = rememberNavController()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val start = if (currentUser != null) "home" else "signin"
        NavHost(
            navController = navController,
            startDestination = start
        ) {
            composable("signin") {
                SignInScreen(navController)
            }
            composable("signup") {
                SignUpScreen(navController)
            }
            composable("passwordreset") {
                PasswordResetScreen(navController)
            }
            composable("home") {
                Homepage(navController)
            }
            composable("profile/{userID}") { backStackEntry ->
                UserProfile(navController, backStackEntry.arguments?.getString("userID") ?: "")
            }
            composable("UpdateUser") {
                UpdateUserData(navController)
            }
            composable("UpdateProfilePicture") {
                UpdateProfilePicture(navController)
            }
            composable("UpdateShowcaseImage") {
                UpdateShowcaseImage(navController)
            }
            composable("UploadImage") {
                UploadImageScreen(navController)
            }
            composable("chat/{channelId}/{channelName}", arguments = listOf(
                navArgument("channelId") {
                    type = NavType.StringType
                },
                navArgument("channelName") {
                    type = NavType.StringType
                }
            )) {
                val channelId = it.arguments?.getString("channelId") ?: ""
                val channelName = it.arguments?.getString("channelName") ?: "Chat"
                ChatScreen(navController, channelId, channelName)
            }
            composable("channels") {
                ChannelsScreen(navController)
            }
            composable("commissions/{userID}") { backStackEntry ->
                CommissionScreen(navController, backStackEntry.arguments?.getString("userID") ?: "")
            }
            composable("search/{query}") { backStackEntry ->
                SearchResults(navController, backStackEntry.arguments?.getString("query") ?: "")
            }
            composable("viewCommissions/{commissionerID}") { backStackEntry ->
                val commissionerID = backStackEntry.arguments?.getString("commissionerID") ?: ""
                val viewModel: CommissionViewModel = hiltViewModel()
                println("Commissioner ID: $commissionerID")
                ViewCommissionsScreen(
                    viewModel = viewModel,
                    commissionerID = commissionerID,
                    navController = navController
                )
            }
            composable("ViewCommissionsAsArtist/{artistID}") { backStackEntry ->
                val viewModel: artistCommissionViewModel = hiltViewModel()
                val artistID = backStackEntry.arguments?.getString("artistID") ?: ""
                println("ArtistID: $artistID")
                ViewCommissionsAsArtist(
                    artistID = artistID,
                    viewModel = viewModel,
                    navController = navController
                )
            }
            composable("commissionDetailsScreen/{commissionID}") { backStackEntry ->
                val viewModel: commissionDetailsViewModel = hiltViewModel()
                val commissionID = backStackEntry.arguments?.getString("commissionID") ?: ""
                println("Commission ID: $commissionID")
                CommissionDetailsScreen(
                    commissionID = commissionID,
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable("ratings/{artistID}") { backStackEntry ->
                RatingsScreen(navController, backStackEntry.arguments?.getString("artistID") ?: "")
            }

            composable("paymentScreen") {
                PaymentScreen(navController)
            }
        }
    }
}

