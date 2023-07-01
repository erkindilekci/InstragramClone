package com.erkindilekci.instagramclone.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.erkindilekci.instagramclone.data.model.PostData
import com.erkindilekci.instagramclone.presentation.util.SharedViewModel
import com.erkindilekci.instagramclone.util.Screen

data class PostRow(
    var post1: PostData? = null,
    var post2: PostData? = null,
    var post3: PostData? = null
) {
    fun isFull() = post1 != null && post2 != null && post3 != null
    fun add(post: PostData) {
        if (post1 == null) {
            post1 = post
        } else if (post2 == null) {
            post2 = post
        } else if (post3 == null) {
            post3 = post
        }
    }
}

@Composable
fun MyPostsScreen(
    navController: NavController,
    viewModel: SharedViewModel
) {
    val userData = viewModel.userData.value
    val isLoading = viewModel.inProgress.value

    val postsLoading = viewModel.refreshPostsProgress.value
    val posts = viewModel.posts.value

    val followers = viewModel.followers.value

    Column {
        Column(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Row {
                ProfileImage(userData?.imageUrl)

                Text(
                    text = "${posts.size}\nposts",
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$followers\nfollowers",
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${userData?.following?.size ?: 0}\nfollowing",
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Column(modifier = Modifier.padding(8.dp)) {
                val usernameDisplay =
                    if (userData?.username == null) "" else "@${userData?.username}"
                userData?.let {
                    it.name?.let {
                        if (it.isNotEmpty()) {
                            Text(
                                text = userData.name ?: "",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Text(text = usernameDisplay, color = MaterialTheme.colorScheme.onSurface)

                    it.bio?.let {
                        if (it.isNotEmpty()) {
                            Text(
                                text = userData.bio ?: "",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
            OutlinedButton(
                onClick = { navigateTo(navController, Screen.Profile) },
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    disabledElevation = 0.dp
                ),
                shape = RoundedCornerShape(15)
            ) {
                Text(text = "Edit Profile")
            }
            PostList(
                isContextLoading = isLoading,
                postsLoading = postsLoading,
                posts = posts,
                modifier = Modifier
                    .weight(1f)
                    .padding(1.dp)
                    .fillMaxSize()
            )
        }
        BottomNavigationMenu(
            selectedItem = BottomNavigationItem.POSTS,
            navController = navController
        )
    }

    if (isLoading) {
        CommonProgressSpinner()
    }

}

@Composable
fun ProfileImage(imageUrl: String?) {
    Box(modifier = Modifier.padding(top = 16.dp)) {

        UserImageCard(
            userImage = imageUrl, modifier = Modifier
                .padding(8.dp)
                .size(110.dp)
        )
    }
}

@Composable
fun PostList(
    isContextLoading: Boolean,
    postsLoading: Boolean,
    posts: List<PostData>,
    modifier: Modifier
) {
    if (postsLoading) {
        CommonProgressSpinner()
    } else if (posts.isEmpty()) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!isContextLoading) Text(text = "No posts available")
        }
    } else {
        LazyColumn(modifier = modifier) {

            val rows = arrayListOf<PostRow>()
            var currentRow = PostRow()
            rows.add(currentRow)
            for (post in posts) {
                if (currentRow.isFull()) {
                    currentRow = PostRow()
                    rows.add(currentRow)
                }
                currentRow.add(post = post)
            }

            items(items = rows) { row ->
                PostsRow(item = row)
            }
        }
    }
}

@Composable
fun PostsRow(item: PostRow) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        PostImage(imageUrl = item.post1?.postImage, modifier = Modifier.weight(1f))
        PostImage(imageUrl = item.post2?.postImage, modifier = Modifier.weight(1f))
        PostImage(imageUrl = item.post3?.postImage, modifier = Modifier.weight(1f))
    }
}

@Composable
fun PostImage(imageUrl: String?, modifier: Modifier) {
    Box(modifier = modifier) {
        var modifier = Modifier
            .padding(1.dp)
            .fillMaxSize()
        if (imageUrl == null) {
            modifier = modifier.clickable(enabled = false) {}
        }
        CommonImage(data = imageUrl, modifier = modifier, contentScale = ContentScale.Crop)
    }
}
