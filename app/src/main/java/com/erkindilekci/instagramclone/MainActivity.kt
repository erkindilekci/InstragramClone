package com.erkindilekci.instagramclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.erkindilekci.instagramclone.presentation.util.theme.InstagramCloneTheme
import com.erkindilekci.instagramclone.util.Navigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstagramCloneTheme {
                Navigation()
            }
        }
    }
}
