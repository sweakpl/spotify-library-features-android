package com.sweak.spotifylibraryfeatures.features.main

import androidx.lifecycle.*
import com.sweak.spotifylibraryfeatures.data.database.entity.Track
import com.sweak.spotifylibraryfeatures.data.repository.FeaturesRepository
import com.sweak.spotifylibraryfeatures.data.repository.SavedTracksRepository
import com.sweak.spotifylibraryfeatures.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedTracksRepository: SavedTracksRepository,
    private val featuresRepository: FeaturesRepository
) : ViewModel() {

    private val refreshTriggerChannel = Channel<Refresh>()
    private val refreshTrigger = refreshTriggerChannel.receiveAsFlow()

    @ExperimentalCoroutinesApi
    val savedTracks = refreshTrigger.flatMapLatest { refresh ->
        val savedTracks = savedTracksRepository.getSavedTracks(refresh == Refresh.FORCE)
        savedTracks
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    @ExperimentalCoroutinesApi
    fun onStart() {
        if (savedTracks.value !is Resource.Loading) {
            viewModelScope.launch {
                refreshTriggerChannel.send(Refresh.NORMAL)
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun onRefresh() {
        if (savedTracks.value !is Resource.Loading) {
            viewModelScope.launch {
                refreshTriggerChannel.send(Refresh.FORCE)
            }
        }
    }

    suspend fun saveTrackFeatures(tracks: List<Track>) {
        featuresRepository.saveTrackFeatures(tracks)
    }

    enum class Refresh {
        NORMAL, FORCE
    }
}