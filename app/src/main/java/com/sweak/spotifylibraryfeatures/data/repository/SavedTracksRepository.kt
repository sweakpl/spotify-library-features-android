package com.sweak.spotifylibraryfeatures.data.repository

import com.sweak.spotifylibraryfeatures.data.database.entity.Track
import com.sweak.spotifylibraryfeatures.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

interface SavedTracksRepository {

    @ExperimentalCoroutinesApi
    fun getSavedTracks(forceRefresh: Boolean): Flow<Resource<List<Track>>>

    suspend fun getTracks(): List<Track>
}