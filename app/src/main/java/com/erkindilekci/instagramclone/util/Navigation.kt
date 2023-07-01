package com.erkindilekci.instagramclone.util

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.erkindilekci.instagramclone.presentation.components.MyPostsScreen
import com.erkindilekci.instagramclone.presentation.components.NewPostScreen
import com.erkindilekci.instagramclone.presentation.components.NotificationMessage
import com.erkindilekci.instagramclone.presentation.screen.authscreen.LoginScreen
import com.erkindilekci.instagramclone.presentation.screen.authscreen.SignupScreen
import com.erkindilekci.instagramclone.presentation.screen.feedscreen.FeedScreen
import com.erkindilekci.instagramclone.presentation.screen.profilescreen.ProfileScreen
import com.erkindilekci.instagramclone.presentation.screen.addscreen.AddScreen
import com.erkindilekci.instagramclone.presentation.util.SharedViewModel

@Composable
fun Navigation() {
    val viewModel = hiltViewModel<SharedViewModel>()
    val navController = rememberNavController()

    NotificationMessage(viewModel = viewModel)

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Signup.route) {
            SignupScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.Feed.route) {
            FeedScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.Add.route) {
            AddScreen(navController = navController)
        }
        composable(Screen.MyPosts.route) {
            MyPostsScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.NewPost.route) { navBackStackEntry ->
            val imageUri = navBackStackEntry.arguments?.getString("imageUri")
            imageUri?.let {
                NewPostScreen(navController = navController, viewModel = viewModel, encodedUri = it)
            }
        }
    }
}
