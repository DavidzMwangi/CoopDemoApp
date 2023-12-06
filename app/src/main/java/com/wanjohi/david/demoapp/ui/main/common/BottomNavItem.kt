package com.wanjohi.david.demoapp.ui.main.common

import com.wanjohi.david.demoapp.R
import com.wanjohi.david.demoapp.ui.main.destinations.Destination
import com.wanjohi.david.demoapp.ui.main.destinations.HomeScreenDestination
import com.wanjohi.david.demoapp.ui.main.destinations.UploadScreenDestination


sealed class BottomNavItem(var title: String, var icon: Int, var destination: Destination) {
    object Home : BottomNavItem(
        title = "Home",
        icon = R.drawable.ic_home,
        destination = HomeScreenDestination
    )
    object Upload: BottomNavItem(
        title = "Upload",
        icon = R.drawable.ic_action_upload,
        destination = UploadScreenDestination
    )
}