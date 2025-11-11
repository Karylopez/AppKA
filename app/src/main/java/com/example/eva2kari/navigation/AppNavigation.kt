package com.example.eva2kari.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eva2kari.ui.screens.auth.LoginScreen
import com.example.eva2kari.ui.screens.camera.CameraScreen
import com.example.eva2kari.ui.screens.home.HomeScreen
import com.example.eva2kari.ui.screens.location.LocationScreen
import com.example.eva2kari.ui.screens.profile.ProfileScreen
import com.example.eva2kari.viewmodel.AuthViewModel
import com.example.eva2kari.viewmodel.ProfileViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Camera : Screen("camera")
    object Location : Screen("location")
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    navController: NavHostController = rememberNavController()
) {
    val currentUser by authViewModel.currentUser.collectAsState()

    val startDestination = if (currentUser != null) {
        Screen.Home.route
    } else {
        Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(authViewModel, navController)
        }

        composable(Screen.Home.route) {
            HomeScreen(authViewModel, navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(authViewModel, profileViewModel, navController)
        }

        composable(Screen.Camera.route) {
            CameraScreen(profileViewModel, navController)
        }

        composable(Screen.Location.route) {
            LocationScreen(navController)
        }
    }
}