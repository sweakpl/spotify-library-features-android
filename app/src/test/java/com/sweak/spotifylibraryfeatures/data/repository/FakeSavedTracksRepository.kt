package com.sweak.spotifylibraryfeatures.data.repository

import com.sweak.spotifylibraryfeatures.data.database.entity.Track
import com.sweak.spotifylibraryfeatures.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import java.util.*

class FakeSavedTracksRepository : SavedTracksRepository {

    private val tracks = mutableListOf<Track>()
    var shouldReturnNetworkError = false

    @ExperimentalCoroutinesApi
    override fun getSavedTracks(forceRefresh: Boolean): Flow<Resource<List<Track>>> = flow {
        emit(Resource.Loading(Collections.unmodifiableList(tracks)))

        if (shouldReturnNetworkError)
            emit(
                Resource.Error(
                    Exception("Network exception"),
                    Collections.unmodifiableList(tracks)
                )
            )
        else
            emit(Resource.Success(Collections.unmodifiableList(tracks)))
    }

    fun setTracks(tracks: List<Track>) {
        this.tracks.clear()
        this.tracks.addAll(tracks)
    }
}