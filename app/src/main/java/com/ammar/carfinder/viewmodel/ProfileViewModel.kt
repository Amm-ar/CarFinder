package com.ammar.carfinder.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ammar.carfinder.data.repository.ProfileRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class ProfileUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val session: UserSession? = null,
    val updateSuccess: Boolean = false,
    val isLoggedOut: Boolean = false
) {
    val fullName: String
        get() = session?.user?.userMetadata?.get("full_name")?.toString()?.removeSurrounding("\"") ?: ""
    val phone: String
        get() = session?.user?.userMetadata?.get("phone")?.toString()?.removeSurrounding("\"") ?: ""
    val avatarUrl: String?
        get() = session?.user?.userMetadata?.get("avatar_url")?.toString()?.removeSurrounding("\"")
}

class ProfileViewModel(
    private val repository: ProfileRepository = ProfileRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserSession()
    }

    fun loadUserSession() {
        _uiState.update { it.copy(session = repository.auth.currentSessionOrNull()) }
    }

    fun updateProfile(fullName: String, phone: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.updateProfile(fullName, phone)
                _uiState.update { it.copy(isLoading = false, updateSuccess = true) }
                loadUserSession() // Refresh session data
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Update failed") }
            }
        }
    }

    fun uploadProfileImage(context: Context, uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.uploadProfileImage(context, uri)
                _uiState.update { it.copy(isLoading = false, updateSuccess = true) }
                loadUserSession() // Refresh session data
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Image upload failed") }
            }
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            repository.auth.signOut()
            _uiState.update { it.copy(session = null, isLoggedOut = true) }
        }
    }
    
    fun resetUpdateStatus() {
        _uiState.update { it.copy(updateSuccess = false, error = null) }
    }

}
