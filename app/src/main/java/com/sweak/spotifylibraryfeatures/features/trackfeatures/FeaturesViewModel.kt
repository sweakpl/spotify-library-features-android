package com.sweak.spotifylibraryfeatures.features.trackfeatures

import androidx.lifecycle.ViewModel
import com.sweak.spotifylibraryfeatures.data.database.entity.TrackFeatures
import com.sweak.spotifylibraryfeatures.data.repository.FeaturesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FeaturesViewModel @Inject constructor(
    private val featuresRepository: FeaturesRepository
) : ViewModel() {

    lateinit var trackFeatures: Flow<List<TrackFeatures>>

    fun initializeTrackFeatures(trackId: String?) {
        trackFeatures = featuresRepository.getTrackFeatures(trackId!!)
    }
}