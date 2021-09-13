package com.sweak.spotifylibraryfeatures.di

import android.app.Application
import androidx.room.Room
import com.sweak.spotifylibraryfeatures.data.api.SpotifyApi
import com.sweak.spotifylibraryfeatures.data.database.Database
import com.sweak.spotifylibraryfeatures.data.database.SavedTracksDao
import com.sweak.spotifylibraryfeatures.data.database.TrackFeaturesDao
import com.sweak.spotifylibraryfeatures.data.repository.DefaultFeaturesRepository
import com.sweak.spotifylibraryfeatures.data.repository.DefaultSavedTracksRepository
import com.sweak.spotifylibraryfeatures.data.repository.FeaturesRepository
import com.sweak.spotifylibraryfeatures.data.repository.SavedTracksRepository
import com.sweak.spotifylibraryfeatures.util.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(SpotifyApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideSpotifyApi(retrofit: Retrofit): SpotifyApi =
        retrofit.create(SpotifyApi::class.java)

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
    fun providePreferences(app: Application): Preferences =
        Preferences(app)

    @Provides
    @Singleton
    fun provideDefaultFeaturesRepository(
        db: Database,
        api: SpotifyApi,
        dao: TrackFeaturesDao,
        preferences: Preferences
    ): FeaturesRepository =
        DefaultFeaturesRepository(db, api, dao, preferences)

    @Provides
    @Singleton
    fun provideDefaultSavedTracksRepository(
        db: Database,
        api: SpotifyApi,
        dao: SavedTracksDao,
        preferences: Preferences
    ): SavedTracksRepository =
        DefaultSavedTracksRepository(db, api, dao, preferences)
}