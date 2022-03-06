@file:Suppress("FunctionName")

package dev.denisbuketa.studdy.ui.screens

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.denisbuketa.studdy.navigation.BottomNavItem
import dev.denisbuketa.studdy.navigation.NavigationGraph

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { HomeScreenBottomNavigation(navController) }
    ) {
        NavigationGraph(navController)
    }
}

@Composable
fun HomeScreenBottomNavigation(navController: NavController) {
    val navItems = listOf(
        BottomNavItem.Study,
        BottomNavItem.Rest
    )
    BottomNavigation(
        elevation = 4.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        navItems.forEach { navItem ->
            BottomNavigationItem(
                icon = { Icon(painter = painterResource(navItem.icon), contentDescription = navItem.title) },
                label = { Text(text = navItem.title, fontSize = 8.sp) },
                selected = currentRoute == navItem.screenRoute,
                onClick = { navController.navigate(navItem.screenRoute) }
            )
        }
    }
}