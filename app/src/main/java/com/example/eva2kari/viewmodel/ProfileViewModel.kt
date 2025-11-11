package com.example.eva2kari.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eva2kari.data.model.User
import com.example.eva2kari.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    object Success : ProfileState()
    data class Error(val message: String) : ProfileState()
}

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        observeCurrentUser()
    }

    private fun observeCurrentUser() {
        viewModelScope.launch {
            repository.getCurrentUserFlow().collect { user ->
                _currentUser.value = user
            }
        }
    }

    fun updateUserImage(userId: Int, imagePath: String) {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            try {
                repository.updateUserImage(userId, imagePath)
                _profileState.value = ProfileState.Success
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(
                    e.message ?: "Error al actualizar imagen"
                )
            }
        }
    }

    fun resetProfileState() {
        _profileState.value = ProfileState.Idle
    }
}