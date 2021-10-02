package com.sweak.spotifylibraryfeatures.di

import com.sweak.spotifylibraryfeatures.data.api.SpotifyApi
import com.sweak.spotifylibraryfeatures.data.database.Database
import com.sweak.spotifylibraryfeatures.data.database.SavedTracksDao
import com.sweak.spotifylibraryfeatures.data.database.TrackFeaturesDao
import com.sweak.spotifylibraryfeatures.data.repository.DefaultFeaturesRepository
import com.sweak.spotifylibraryfeatures.data.repository.DefaultSavedTracksRepository
import com.sweak.spotifylibraryfeatures.data.repository.FeaturesRepository
import com.sweak.spotifylibraryfeatures.data.repository.SavedTracksRepository
import com.sweak.spotifylibraryfeatures.util.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDefaultFeaturesRepository(
        db: Database,
        api: SpotifyApi,
        dao: TrackFeaturesDao,
        dataStoreManager: DataStoreManager
    ): FeaturesRepository =
        DefaultFeaturesRepository(db, api, dao, dataStoreManager)

    @Provides
    @Singleton
    fun provideDefaultSavedTracksRepository(
        db: Database,
        api: SpotifyApi,
        dao: SavedTracksDao,
        dataStoreManager: DataStoreManager
    ): SavedTracksRepository =
        DefaultSavedTracksRepository(db, api, dao, dataStoreManager)
}