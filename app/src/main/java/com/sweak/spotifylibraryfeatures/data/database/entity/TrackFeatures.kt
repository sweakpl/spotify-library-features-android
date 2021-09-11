package com.sweak.spotifylibraryfeatures.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_features")
data class TrackFeatures(
    @PrimaryKey val id: String,
    val danceability: Double,
    val energy: Double,
    val loudness: Double,
    val speechiness: Double,
    val acousticness: Double,
    val instrumentalness: Double,
    val liveness: Double,
    val valence: Double,
    val tempo: Double
)
