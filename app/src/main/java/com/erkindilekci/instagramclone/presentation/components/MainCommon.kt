package com.erkindilekci.instagramclone.presentation.components

import android.os.Parcelable
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.erkindilekci.instagramclone.R
import com.erkindilekci.instagramclone.presentation.util.SharedViewModel
import com.erkindilekci.instagramclone.util.Screen

@Composable
fun NotificationMessage(viewModel: SharedViewModel) {
    val notificationState = viewModel.popupNotification.value
    val notificationMessage = notificationState?.getContentOrNull()
    if (notificationMessage != null) {
        Toast.makeText(LocalContext.current, notificationMessage, Toast.LENGTH_LONG).show()
    }
}

@Composable
fun CommonProgressSpinner() {
    Row(
        modifier = Modifier
            .alpha(0.5f)
            .background(Color.LightGray)
            .clickable(enabled = false) { }
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator()
    }
}

data class NavParam(
    val name: String,
    val value: Parcelable
)

fun navigateTo(navController: NavController, dest: Screen, vararg params: NavParam) {
    for (param in params) {
        navController.currentBackStackEntry?.arguments?.putParcelable(param.name, param.value)
    }
    navController.navigate(dest.route) {
        popUpTo(dest.route)
        launchSingleTop = true
    }
}

@Composable
fun CheckSignedIn(viewModel: SharedViewModel, navController: NavController) {
    val alreadyLoggedIn = remember { mutableStateOf(false) }
    val signedIn = viewModel.signedIn.value
    if (signedIn && !alreadyLoggedIn.value) {
        alreadyLoggedIn.value = true
        navController.navigate(Screen.Feed.route) {
            popUpTo(0)
        }
    }
}

@Composable
fun CommonImage(
    data: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    data?.let {
        AsyncImage(
            model = data,
            contentDescription = null,
            modifier = modifier.wrapContentSize(),
            contentScale = contentScale,
            placeholder = painterResource(
                id = R.drawable.loading
            )
        )
    }
}

@Composable
fun UserImageCard(
    userImage: String?,
    modifier: Modifier = Modifier
) {
    Card(
        shape = CircleShape, modifier = modifier
            .padding(8.dp)
            .size(64.dp)
    ) {
        if (userImage.isNullOrEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.ic_user),
                modifier = Modifier
                    .size(55.dp)
                    .align(Alignment.CenterHorizontally),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.Gray)
            )
        } else {
            CommonImage(data = userImage)
        }
    }
}

@Composable
fun CommonDivider() {
    Divider(
        color = Color.LightGray,
        thickness = 1.dp,
        modifier = Modifier
            .alpha(0.3f)
            .padding(top = 8.dp, bottom = 8.dp)
    )
}

private enum class LikeIconSize {
    SMALL,
    LARGE
}

@Composable
fun LikeAnimation(like: Boolean = true) {
    var sizeState by remember { mutableStateOf(LikeIconSize.SMALL) }
    val transition = updateTransition(targetState = sizeState, label = "")
    val size by transition.animateDp(
        label = "",
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        }
    ) { state ->
        when (state) {
            LikeIconSize.SMALL -> 0.dp
            LikeIconSize.LARGE -> 150.dp
        }
    }

    Image(
        painter = painterResource(id = if (like) R.drawable.ic_like else R.drawable.ic_dislike),
        contentDescription = null,
        modifier = Modifier.size(size = size),
        colorFilter = ColorFilter.tint(if (like) Color.Red else Color.Gray)
    )
    sizeState = LikeIconSize.LARGE
}
