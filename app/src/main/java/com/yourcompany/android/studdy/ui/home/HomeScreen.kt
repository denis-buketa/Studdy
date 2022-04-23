/*
 * Copyright (c) 2022 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

@file:Suppress("FunctionName")

package com.yourcompany.android.studdy.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yourcompany.android.studdy.alarm.ExactAlarms
import com.yourcompany.android.studdy.alarm.InexactAlarms
import com.yourcompany.android.studdy.alarm.PreviewExactAlarms
import com.yourcompany.android.studdy.alarm.PreviewInexactAlarms
import com.yourcompany.android.studdy.navigation.BottomNavItem

@Composable
fun HomeScreen(
    exactAlarms: ExactAlarms,
    inexactAlarms: InexactAlarms,
    onSchedulingAlarmNotAllowed: () -> Unit,
    showStopAlarmButton: Boolean,
    onStopAlarmClicked: () -> Unit
) {
  val navController = rememberNavController()
  Scaffold(
      topBar = { HomeScreenTopBar() },
      bottomBar = { HomeScreenBottomNavigation(navController) }
  ) { scaffoldPadding ->
    NavHost(navController, startDestination = BottomNavItem.Study.screenRoute) {
      composable(BottomNavItem.Study.screenRoute) {
        StudyTab(exactAlarms, onSchedulingAlarmNotAllowed)
      }
      composable(BottomNavItem.Rest.screenRoute) {
        RestTab(inexactAlarms)
      }
    }

    if (showStopAlarmButton) {
      Box(modifier = Modifier
          .fillMaxSize()
          .padding(scaffoldPadding)) {
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp),
            elevation = 8.dp,
            color = MaterialTheme.colors.secondary
        ) {
          Box(modifier = Modifier
              .fillMaxSize()
              .clickable { onStopAlarmClicked.invoke() }) {
            Text(
                text = "Stop Alarm",
                modifier = Modifier.align(Alignment.Center),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.surface
            )
          }

        }
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

    navItems.forEach { navItem ->
      BottomNavigationItem(
          icon = {
            Icon(
                painter = painterResource(navItem.icon),
                contentDescription = navItem.title
            )
          },
          label = { Text(text = navItem.title, fontSize = 8.sp) },
          selected = navBackStackEntry?.destination?.route == navItem.screenRoute,
          onClick = {
            navController.apply {
              popBackStack()
              navigate(navItem.screenRoute)
            }
          }
      )
    }
  }
}

@Preview
@Composable
fun HomeScreenPreview() {
  HomeScreen(PreviewExactAlarms, PreviewInexactAlarms, {}, true) {}
}