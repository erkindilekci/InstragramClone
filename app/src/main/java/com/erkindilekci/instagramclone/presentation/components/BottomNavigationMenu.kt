package com.erkindilekci.instagramclone.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.erkindilekci.instagramclone.R
import com.erkindilekci.instagramclone.util.Screen

@Composable
fun BottomNavigationMenu(
    selectedItem: BottomNavigationItem,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (item in BottomNavigationItem.values()) {
            Image(
                painter = painterResource(id = item.icon),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(5.dp)
                    .weight(1f)
                    .clickable { navigateTo(navController, item.navDestination) },
                colorFilter = if (item == selectedItem) ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                else ColorFilter.tint(Color.LightGray)
            )
        }
    }
}

enum class BottomNavigationItem(val icon: Int, val navDestination: Screen) {
    FEED(R.drawable.ic_home, Screen.Feed),
    ADD(R.drawable.ic_addpost, Screen.Add),
    POSTS(R.drawable.ic_posts, Screen.MyPosts)
}
