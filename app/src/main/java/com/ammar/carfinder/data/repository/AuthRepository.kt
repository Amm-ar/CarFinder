package com.ammar.carfinder.data.repository

import com.ammar.carfinder.data.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class AuthRepository {

    val auth = SupabaseClient.client.auth

    suspend fun isUserLoggedIn(): Boolean {
        return auth.currentSessionOrNull() != null
    }

    suspend fun getCurrentUser() = auth.currentUserOrNull()

    suspend fun signUp(email: String, password: String) = auth.signUpWith(Email) {
        this.email = email
        this.password = password
    }

    suspend fun signIn(email: String, password: String) = auth.signInWith(Email) {
        this.email = email
        this.password = password
    }

    suspend fun signOut() = auth.signOut()

}
