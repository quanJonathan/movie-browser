package com.hitachivantara.mobilecoe.android

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.hitachivantara.mobilecoe.android.data.MovieRepository
import com.hitachivantara.mobilecoe.android.data.api.ApiService
import com.hitachivantara.mobilecoe.android.data.api.NetworkModule
import com.hitachivantara.mobilecoe.android.data.database.AppDatabase
import com.hitachivantara.mobilecoe.android.data.preferences.PreferencesStore
import com.hitachivantara.mobilecoe.android.ui.movies.MoviesViewModelFactory

object Injection {

    private fun provideMovieRepository(context: Context, preferencesStore: PreferencesStore): MovieRepository{
        return MovieRepository(provideNetWorkModule(preferencesStore).createService(ApiService::class.java), AppDatabase.getDatabase(context))
    }

    private fun providePreferenceStore(context: Context): PreferencesStore{
        return PreferencesStore.getInstance(context)
    }

    fun provideMovieViewModelFactory(context: Context, owner: SavedStateRegistryOwner): ViewModelProvider.Factory{
        return MoviesViewModelFactory(owner, provideMovieRepository(context, providePreferenceStore(context)))
    }

    private fun provideNetWorkModule(preferencesStore: PreferencesStore): NetworkModule{
        return NetworkModule(preferencesStore)
    }
}