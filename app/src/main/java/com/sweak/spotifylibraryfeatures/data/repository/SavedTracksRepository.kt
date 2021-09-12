package com.sweak.spotifylibraryfeatures.data.repository

import androidx.room.withTransaction
import com.sweak.spotifylibraryfeatures.data.api.SpotifyApi
import com.sweak.spotifylibraryfeatures.data.database.Database
import com.sweak.spotifylibraryfeatures.data.database.SavedTracksDao
import com.sweak.spotifylibraryfeatures.data.database.entity.Track
import com.sweak.spotifylibraryfeatures.util.Preferences
import com.sweak.spotifylibraryfeatures.util.Preferences.Companion.PREFERENCES_EXPIRY_DATE_KEY
import com.sweak.spotifylibraryfeatures.util.Resource
import com.sweak.spotifylibraryfeatures.util.networkBoundResource
import com.sweak.spotifylibraryfeatures.util.parseSavedTracksBatch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class SavedTracksRepository @Inject constructor(
    private val db: Database,
    private val api: SpotifyApi,
    private val dao: SavedTracksDao,
    private val preferences: Preferences
) {

    @ExperimentalCoroutinesApi
    fun getSavedTracks(
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
                    val expiryDate = preferences.getLong(PREFERENCES_EXPIRY_DATE_KEY)
                    currentDate < expiryDate
                }
            }
        )

    private suspend fun getTracks(): List<Track> {
        val tracks = mutableListOf<Track>()
        var offset = 0
        val limit = 20

        do {
            val tracksBatch = api.getSavedTracks(
                "Bearer ${preferences.getString(Preferences.PREFERENCES_ACCESS_TOKEN_KEY)}",
                offset, limit
            )
            tracks.addAll(parseSavedTracksBatch(tracksBatch))
            offset += limit
        } while (tracksBatch.next != null)

        return tracks
    }
}