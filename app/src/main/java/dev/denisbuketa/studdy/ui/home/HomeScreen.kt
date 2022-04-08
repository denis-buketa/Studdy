@file:Suppress("FunctionName")

package dev.denisbuketa.studdy.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.denisbuketa.studdy.alarm.ExactAlarms
import dev.denisbuketa.studdy.alarm.InexactAlarms
import dev.denisbuketa.studdy.alarm.PreviewExactAlarms
import dev.denisbuketa.studdy.alarm.PreviewInexactAlarms
import dev.denisbuketa.studdy.navigation.BottomNavItem

@Composable
fun HomeScreen(
    exactAlarms: ExactAlarms,
    inexactAlarms: InexactAlarms,
    onSchedulingAlarmNotAllowed: () -> Unit
) {
  val navController = rememberNavController()
  Scaffold(
      topBar = { HomeScreenTopBar() },
      bottomBar = { HomeScreenBottomNavigation(navController) }
  ) {
    NavHost(navController, startDestination = BottomNavItem.Study.screenRoute) {
      composable(BottomNavItem.Study.screenRoute) {
        StudyTab(exactAlarms, onSchedulingAlarmNotAllowed)
      }
      composable(BottomNavItem.Rest.screenRoute) {
        RestTab(inexactAlarms)
      }
    }
  }
}

@Composable
private fun HomeScreenTopBar() {
  TopAppBar(contentPadding = PaddingValues(start = 8.dp, end = 8.dp)) {
    Text(
        text = "Studdy App",
        fontSize = 24.sp
    )
  }
}

@Composable
private fun HomeScreenBottomNavigation(navController: NavController) {
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
          icon = {
            Icon(
                painter = painterResource(navItem.icon),
                contentDescription = navItem.title
            )
          },
          label = { Text(text = navItem.title, fontSize = 8.sp) },
          selected = currentRoute == navItem.screenRoute,
          onClick = { navController.navigate(navItem.screenRoute) }
      )
    }
  }
}

@Preview
@Composable
fun HomeScreenPreview() {
  HomeScreen(PreviewExactAlarms, PreviewInexactAlarms) {}
}