package dev.denisbuketa.studdy.navigation

import dev.denisbuketa.studdy.R

sealed class BottomNavItem(var title: String, var icon: Int, var screenRoute: String) {
  object Study : BottomNavItem("Study", R.drawable.ic_book, "study")
  object Rest : BottomNavItem("Rest", R.drawable.ic_rest, "rest")
}