package com.erkindilekci.instagramclone.presentation.screen.feedscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.erkindilekci.instagramclone.R
import com.erkindilekci.instagramclone.data.model.PostData
import com.erkindilekci.instagramclone.presentation.components.BottomNavigationItem
import com.erkindilekci.instagramclone.presentation.components.BottomNavigationMenu
import com.erkindilekci.instagramclone.presentation.components.CommonImage
import com.erkindilekci.instagramclone.presentation.components.CommonProgressSpinner
import com.erkindilekci.instagramclone.presentation.components.LikeAnimation
import com.erkindilekci.instagramclone.presentation.util.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(
    navController: NavController,
    viewModel: SharedViewModel
) {
    val userDataLoading = viewModel.inProgress.value
    val userData = viewModel.userData.value
    val personalizedFeed = viewModel.postsFeed.value
    val personalizedFeedLoading = viewModel.postsFeedProgress.value

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.insta),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 12.dp, bottom = 4.dp)
                        .height(38.dp)
                )
            }
        },
        content = {
            PostsList(
                posts = personalizedFeed,
                modifier = Modifier.padding(it),
                loading = personalizedFeedLoading or userDataLoading,
                viewModel = viewModel,
                currentUserId = userData?.userId ?: ""
            )
        },
        bottomBar = {
            BottomNavigationMenu(
                selectedItem = BottomNavigationItem.FEED,
                navController = navController
            )
        }
    )
}

@Composable
fun PostsList(
    posts: List<PostData>,
    modifier: Modifier,
    loading: Boolean,
    viewModel: SharedViewModel,
    currentUserId: String
) {
    Box(modifier = modifier) {
        LazyColumn {
            items(items = posts) {
                Post(
                    post = it,
                    currentUserId = currentUserId,
                    viewModel = viewModel
                )
            }
        }
        if (loading) {
            CommonProgressSpinner()
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Post(
    post: PostData,
    currentUserId: String,
    viewModel: SharedViewModel
) {
    val likeAnimation = remember { mutableStateOf(false) }
    val dislikeAnimation = remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 4.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    shape = CircleShape, modifier = Modifier
                        .padding(4.dp)
                        .size(32.dp)
                ) {
                    CommonImage(data = post.userImage, contentScale = ContentScale.Crop)
                }
                Text(
                    text = post.username ?: "",
                    modifier = Modifier.padding(4.dp),
                    fontWeight = FontWeight.Medium
                )
            }

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                val modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 150.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                if (post.likes?.contains(currentUserId) == true) {
                                    dislikeAnimation.value = true
                                } else {
                                    likeAnimation.value = true
                                }
                                viewModel.onLikePost(post)
                            }
                        )
                    }
                CommonImage(
                    data = post.postImage,
                    modifier = modifier,
                    contentScale = ContentScale.FillWidth
                )

                if (likeAnimation.value) {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(600L)
                        likeAnimation.value = false
                    }
                    LikeAnimation()
                }
                if (dislikeAnimation.value) {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(600L)
                        dislikeAnimation.value = false
                    }
                    LikeAnimation(false)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_like),
                    contentDescription = null,
                    tint = Color.Red
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(text = "${post.likes?.size ?: 0} Likes")
            }

            if (post.postDescription != null) {
                if (post.postDescription.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .padding(horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = post.username ?: "", fontWeight = FontWeight.Medium)

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(text = post.postDescription ?: "", fontSize = 15.5.sp)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}
