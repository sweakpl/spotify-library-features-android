package com.sweak.spotifylibraryfeatures.data.repository

import androidx.room.withTransaction
import com.sweak.spotifylibraryfeatures.data.api.SpotifyApi
import com.sweak.spotifylibraryfeatures.data.database.Database
import com.sweak.spotifylibraryfeatures.data.database.TrackFeaturesDao
import com.sweak.spotifylibraryfeatures.data.database.entity.Track
import com.sweak.spotifylibraryfeatures.data.database.entity.TrackFeatures
import com.sweak.spotifylibraryfeatures.util.Preferences
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class DefaultFeaturesRepository @Inject constructor(
    private val db: Database,
    private val api: SpotifyApi,
    private val dao: TrackFeaturesDao,
    private val preferences: Preferences
) : FeaturesRepository {

    override fun getTrackFeatures(id: String): Flow<List<TrackFeatures>> =
        dao.getTrackFeatures(id)

    override suspend fun saveTrackFeatures(tracks: List<Track>) {
        val trackIds = mutableListOf<String>()

        for (track in tracks)
            trackIds.add(track.id)
        val trackIdChunks = trackIds.chunked(100)

        val allTrackFeatures = mutableListOf<TrackFeatures>()

        for (chunkedTrackIds in trackIdChunks) {
            val trackIdsString = chunkedTrackIds.joinToString(",")

            try {
                allTrackFeatures.addAll(
                    api.getTrackFeatures(
                        "Bearer ${preferences.getString(Preferences.PREFERENCES_ACCESS_TOKEN_KEY)}",
                        trackIdsString
                    ).items
                )
            } catch (e: Exception) {
                if (e !is HttpException && e !is IOException)
                    throw e
                else
                    return
            }
        }

        db.withTransaction {
            dao.deleteAllTrackFeatures()
            dao.insertTrackFeatures(allTrackFeatures)
        }
    }
}