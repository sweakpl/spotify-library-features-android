package com.sweak.spotifylibraryfeatures.di

import android.app.Application
import androidx.room.Room
import com.sweak.spotifylibraryfeatures.data.database.Database
import com.sweak.spotifylibraryfeatures.data.database.SavedTracksDao
import com.sweak.spotifylibraryfeatures.data.database.TrackFeaturesDao
import com.sweak.spotifylibraryfeatures.util.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): Database =
        Room.databaseBuilder(app, Database::class.java, "saved_tracks_database")
            .build()

    @Provides
    @Singleton
    fun provideSavedTracksDao(database: Database): SavedTracksDao =
        database.savedTracksDao()

    @Provides
    @Singleton
    fun provideTrackFeaturesDao(database: Database): TrackFeaturesDao =
        database.trackFeaturesDao()

    @Provides
    @Singleton
    fun provideDataStoreManager(app: Application): DataStoreManager =
        DataStoreManager(app)
}