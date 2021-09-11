package com.sweak.spotifylibraryfeatures.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sweak.spotifylibraryfeatures.data.database.entity.Track
import com.sweak.spotifylibraryfeatures.data.database.entity.TrackFeatures

@Database(entities = [Track::class, TrackFeatures::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun savedTracksDao(): SavedTracksDao
    abstract fun trackFeaturesDao(): TrackFeaturesDao
}