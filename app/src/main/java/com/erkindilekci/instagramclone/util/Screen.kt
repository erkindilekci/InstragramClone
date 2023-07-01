package com.erkindilekci.instagramclone.util

sealed class Screen(val route: String) {
    object Signup : Screen("signup")
    object Login : Screen("login")
    object Feed : Screen("feed")
    object Add : Screen("search")
    object MyPosts : Screen("myposts")
    object Profile : Screen("profile")
    object NewPost : Screen("newpost/{imageUri}") {
        fun passUri(uri: String) = "newpost/$uri"
    }
}
