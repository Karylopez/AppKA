package com.example.eva2kari

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eva2kari.data.local.AppDatabase
import com.example.eva2kari.data.repository.UserRepository
import com.example.eva2kari.navigation.AppNavigation
import com.example.eva2kari.ui.theme.Eva2KariTheme
import com.example.eva2kari.viewmodel.AuthViewModel
import com.example.eva2kari.viewmodel.ProfileViewModel
import com.example.eva2kari.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar base de datos y repository
        val database = AppDatabase.getDatabase(applicationContext)
        val userRepository = UserRepository(database.userDao())
        val viewModelFactory = ViewModelFactory(userRepository)

        setContent {
            Eva2KariTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Crear ViewModels
                    val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
                    val profileViewModel: ProfileViewModel = viewModel(factory = viewModelFactory)

                    // Navegación
                    AppNavigation(
                        authViewModel = authViewModel,
                        profileViewModel = profileViewModel
                    )
                }
            }
        }
    }
}