package com.erkindilekci.instagramclone.presentation.screen.authscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.erkindilekci.instagramclone.R
import com.erkindilekci.instagramclone.presentation.components.CheckSignedIn
import com.erkindilekci.instagramclone.presentation.components.CommonProgressSpinner
import com.erkindilekci.instagramclone.presentation.components.navigateTo
import com.erkindilekci.instagramclone.presentation.util.SharedViewModel
import com.erkindilekci.instagramclone.util.Screen

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: SharedViewModel
) {
    CheckSignedIn(viewModel = viewModel, navController = navController)

    val focusManager = LocalFocusManager.current

    if (!viewModel.signedIn.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 64.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var emailState by remember { mutableStateOf(TextFieldValue()) }
                var passState by remember { mutableStateOf(TextFieldValue()) }

                Image(
                    painter = painterResource(id = R.drawable.ig_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .width(250.dp)
                        .padding(vertical = 32.dp)
                        .padding(8.dp)
                )

                OutlinedTextField(
                    value = emailState,
                    onValueChange = { emailState = it },
                    modifier = Modifier.padding(8.dp),
                    label = { Text(text = "Email") },
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                OutlinedTextField(
                    value = passState,
                    onValueChange = { passState = it },
                    modifier = Modifier.padding(8.dp),
                    label = { Text(text = "Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    )
                )

                Button(
                    onClick = {
                        focusManager.clearFocus(force = true)
                        viewModel.onLogin(emailState.text, passState.text)
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Login")
                }

                Text(
                    text = "New here? Go to signup ->",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            navigateTo(navController, Screen.Signup)
                        }
                )
            }

            val isLoading = viewModel.inProgress.value
            if (isLoading) {
                CommonProgressSpinner()
            }
        }
    }
}
