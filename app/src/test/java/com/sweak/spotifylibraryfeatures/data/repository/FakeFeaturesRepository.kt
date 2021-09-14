package com.sweak.spotifylibraryfeatures.data.repository

import com.sweak.spotifylibraryfeatures.data.database.entity.Track
import com.sweak.spotifylibraryfeatures.data.database.entity.TrackFeatures
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random.Default.nextDouble

class FakeFeaturesRepository : FeaturesRepository {

    private val trackFeatures = mutableListOf<TrackFeatures>()

    override fun getTrackFeatures(id: String): Flow<List<TrackFeatures>> = flow {
        emit(trackFeatures.filter { it.id == id })
    }

    override suspend fun saveTrackFeatures(tracks: List<Track>) {
        val trackFeaturesTemporary = mutableListOf<TrackFeatures>()

        for (track in tracks) {
            trackFeaturesTemporary.add(
                TrackFeatures(
                    track.id,
                    nextDouble(),
                    nextDouble(),
                    nextDouble(),
                    nextDouble(),
                    nextDouble(),
                    nextDouble(),
                    nextDouble(),
                    nextDouble(),
                    nextDouble()
                )
            )
        }

        trackFeatures.clear()
        trackFeatures.addAll(trackFeaturesTemporary)
    }
}