package com.erkindilekci.instagramclone.presentation.screen.addscreen

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.erkindilekci.instagramclone.util.Screen

@Composable
fun AddScreen(
    navController: NavController,
) {
    val newPostImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri ->
        uri?.let {
            val encoded = Uri.encode(it.toString())
            val route = Screen.NewPost.passUri(encoded)
            navController.navigate(route)
        }
    }

    LaunchedEffect(key1 = true) {
        newPostImageLauncher.launch("image/*")
    }

    BackHandler {
        navController.navigate(Screen.Feed.route)
    }
}
