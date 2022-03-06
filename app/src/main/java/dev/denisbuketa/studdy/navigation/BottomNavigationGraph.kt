@file:Suppress("FunctionName")

package dev.denisbuketa.studdy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.denisbuketa.studdy.ui.bottomsheettabs.RestTab
import dev.denisbuketa.studdy.ui.bottomsheettabs.StudyTab

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Study.screenRoute) {
        composable(BottomNavItem.Study.screenRoute) {
            StudyTab()
        }
        composable(BottomNavItem.Rest.screenRoute) {
            RestTab()
        }
    }
}