package com.ammar.carfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ammar.carfinder.ui.AddCarScreen
import com.ammar.carfinder.ui.AuthScreen
import com.ammar.carfinder.ui.CarListScreen
import com.ammar.carfinder.ui.ProfileScreen
import com.ammar.carfinder.ui.theme.CarFinderTheme
import com.ammar.carfinder.viewmodel.CarViewModel
import com.ammar.carfinder.viewmodel.ProfileViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge() // Temporarily disabled to debug compiler crash
        setContent {
            CarFinderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CarFinderApp()
                }
            }
        }
    }
}

@Composable
fun CarFinderApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate("car_list") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }
        composable("car_list") {
            val carViewModel: CarViewModel = viewModel()
            val profileViewModel: ProfileViewModel = viewModel()
            CarListScreen(
                onAddCarClick = {
                    navController.navigate("add_car")
                },
                viewModel = carViewModel,
                profileViewModel = profileViewModel,
                onSettingsClick = {
                    navController.navigate("profile")
                }
            )
        }
        composable("add_car") {
            AddCarScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("profile") {
             val profileViewModel: ProfileViewModel = viewModel()
            ProfileScreen(
                viewModel = profileViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate("auth") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
