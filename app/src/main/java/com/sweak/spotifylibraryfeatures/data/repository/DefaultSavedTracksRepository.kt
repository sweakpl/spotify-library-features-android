package com.sweak.spotifylibraryfeatures.data.repository

import androidx.room.withTransaction
import com.sweak.spotifylibraryfeatures.data.api.SpotifyApi
import com.sweak.spotifylibraryfeatures.data.database.Database
import com.sweak.spotifylibraryfeatures.data.database.SavedTracksDao
import com.sweak.spotifylibraryfeatures.data.database.entity.Track
import com.sweak.spotifylibraryfeatures.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject

class DefaultSavedTracksRepository @Inject constructor(
    private val db: Database,
    private val api: SpotifyApi,
    private val dao: SavedTracksDao,
    private val dataStoreManager: DataStoreManager
) : SavedTracksRepository {

    @ExperimentalCoroutinesApi
    override fun getSavedTracks(
        forceRefresh: Boolean
    ): Flow<Resource<List<Track>>> =
        networkBoundResource(
            query = {
                dao.getAllTracks()
            },
            fetch = {
                val tracks = getTracks()
                tracks
            },
            saveFetchResult = { tracks ->
                db.withTransaction {
                    dao.deleteAllTracks()
                    dao.insertTracks(tracks)
                }
            },
            shouldFetch = {
                if (forceRefresh)
                    true
                else {
                    val currentDate = Calendar.getInstance().time.time
                    val expiryDate = runBlocking {
                        dataStoreManager.getLong(DataStoreManager.EXPIRY_DATE).first()
                    }
                    currentDate < expiryDate
                }
            }
        )

    private suspend fun getTracks(): List<Track> {
        val tracks = mutableListOf<Track>()
        var offset = 0
        val limit = 20

        do {
            var accessToken = DataStoreManager.PREFERENCES_DEFAULT_STRING
            coroutineScope {
                val tokenJob = launch {
                    accessToken = dataStoreManager.getString(DataStoreManager.ACCESS_TOKEN).first()
                }
                tokenJob.join()
            }
            val tracksBatch = api.getSavedTracks(
                "Bearer $accessToken",
                offset, limit
            )
            tracks.addAll(parseSavedTracksBatch(tracksBatch))
            offset += limit
        } while (tracksBatch.next != null)

        return tracks
    }
}