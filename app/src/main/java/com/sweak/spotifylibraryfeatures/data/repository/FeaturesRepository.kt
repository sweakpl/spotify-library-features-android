package com.sweak.spotifylibraryfeatures.data.repository

import com.sweak.spotifylibraryfeatures.data.database.entity.Track
import com.sweak.spotifylibraryfeatures.data.database.entity.TrackFeatures
import kotlinx.coroutines.flow.Flow

interface FeaturesRepository {

    fun getTrackFeatures(id: String): Flow<List<TrackFeatures>>

    suspend fun saveTrackFeatures(tracks: List<Track>)
}