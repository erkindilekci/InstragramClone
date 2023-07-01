package com.erkindilekci.instagramclone.presentation.components

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.erkindilekci.instagramclone.presentation.util.SharedViewModel
import com.erkindilekci.instagramclone.util.Screen

@Composable
fun NewPostScreen(
    navController: NavController,
    viewModel: SharedViewModel,
    encodedUri: String
) {
    val imageUri by remember { mutableStateOf(encodedUri) }
    var description by rememberSaveable { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(scrollState)
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cancel",
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.clickable { navController.popBackStack() }
            )
            Text(text = "Post", color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.clickable {
                focusManager.clearFocus()
                viewModel.onNewPost(
                    Uri.parse(imageUri),
                    description
                ) { navController.navigate(Screen.Feed.route) }
            })
        }

        CommonDivider()

        AsyncImage(
            model = imageUri, contentDescription = null, modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 150.dp),
            contentScale = ContentScale.FillWidth
        )

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 450.dp),
                label = { Text(text = "Description") },
                singleLine = false,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                )
            )
        }
    }

    if (viewModel.inProgress.value) {
        CommonProgressSpinner()
    }

    BackHandler {
        navController.navigate(Screen.Feed.route)
    }
}
