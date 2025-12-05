package com.ammar.carfinder

import android.app.Application
import com.ammar.carfinder.data.SupabaseClient

class CarFinderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Supabase with the application context if needed for advanced auth features
        // However, the current SupabaseClient implementation might be an object.
        // The auth plugin automatically attempts to use EncryptedSharedPreferences if a context is not strictly required for initialization but for loading.
        // But for auth-kt on Android, ideally we initialize it here if we weren't using the singleton object pattern that initializes immediately.
        
        // Since SupabaseClient is an object, it initializes lazily when first accessed.
        // To ensure context is available for session persistence, we often need to configure it.
        // But wait, the SupabaseClient object in this project initializes the client immediately upon class loading.
        // The issue is that `createSupabaseClient` doesn't know about the Android Context by default.
        // We should move the Supabase initialization to a place where we can pass the context, OR simply initialization is fine but we need to Load from storage.
        
        // Actually, correct pattern for Android Auth persistence with Supabase-kt is roughly:
        /*
          install(Auth) {
              storage = EncryptedSharedPreferencesStorage(context) // or similar
          }
        */
        
        // Since SupabaseClient is a singleton object, let's just initialize it or its config here.
        // But a better way is to refactor SupabaseClient to 'init' with context.
        
        SupabaseClient.initialize(this)
    }
}
