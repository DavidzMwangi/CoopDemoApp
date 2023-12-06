package com.wanjohi.david.demoapp.ui.main.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.siddroid.holi.colors.MaterialColor

@Composable
fun StandardScaffold(
    navController: NavController,

    showBottomBar: Boolean = true,
    items: List<BottomNavItem> = listOf(
        BottomNavItem.Home,
        BottomNavItem.Upload
    ),
    content: @Composable (paddingValues: PaddingValues) -> Unit,
) {
    Scaffold(
//        backgroundColor = colorResource(id = R.color.colorPrimaryDark),
        bottomBar = {
            if (showBottomBar) {
//                ConstraintLayout() {
                    BottomNavigation(
                        backgroundColor = if (isSystemInDarkTheme()) MaterialColor.GREY_900 else MaterialColor.WHITE,
                        contentColor = if (isSystemInDarkTheme()) MaterialColor.WHITE  else  MaterialColor.GREY_900,
                        modifier = Modifier
                            .padding(12.dp)
                            .shadow(shape = RoundedCornerShape(10.dp), clip = true, elevation = 16.dp)

                    ) {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        items.forEachIndexed { index,item ->

                            BottomNavigationItem(
                                icon = {
                                    Icon(
                                        painterResource(id = item.icon),
                                        contentDescription = item.title,
                                    )
                                },
                                label = {
                                    Text(
                                        text = item.title,
                                        fontSize = 9.sp
                                    )
                                },
                                selectedContentColor = MaterialColor.PURPLE_500,
                                unselectedContentColor = if (isSystemInDarkTheme()) MaterialColor.WHITE  else  MaterialColor.GREY_900,
                                alwaysShowLabel = true,
                                selected = currentDestination?.route?.contains(item.destination.route) == true,
                                onClick = {
                                    navController.navigate(item.destination.route) {
                                        navController.graph.startDestinationRoute?.let { screen_route ->
                                            popUpTo(screen_route) {
                                                saveState = true
                                            }
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                            )
                        }
                    }


//                }



            }
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}
