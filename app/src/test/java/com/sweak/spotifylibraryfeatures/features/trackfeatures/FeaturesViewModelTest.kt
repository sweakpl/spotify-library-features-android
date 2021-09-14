package com.sweak.spotifylibraryfeatures.features.trackfeatures

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweak.spotifylibraryfeatures.MainCoroutineRule
import com.sweak.spotifylibraryfeatures.data.database.entity.Track
import com.sweak.spotifylibraryfeatures.data.repository.FakeFeaturesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FeaturesViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: FeaturesViewModel
    private lateinit var insertedTracks: List<Track>

    @Before
    fun setup() {
        insertedTracks = listOf(
            Track("idofthesong", "songname", "artists", 1234567, "albumcoverurl"),
            Track("id", "song", "artists", 7654321, "url"),
            Track("idsong", "name", "artists", 9876543, "coverurl")
        )
    }

    @Test
    fun shouldReturnEmptyList_whenNoTrackFeaturesFound() = runBlockingTest {
        viewModel = FeaturesViewModel(
            FakeFeaturesRepository().apply {
                saveTrackFeatures(insertedTracks)
            }
        )
        viewModel.initializeTrackFeatures("nonexistentid")

        val retrievedFeatures = viewModel.trackFeatures.first()

        assertTrue(retrievedFeatures.isEmpty())
    }

    @Test
    fun shouldReturnOneTrack_whenOneTrackInitialized() = runBlockingTest {
        viewModel = FeaturesViewModel(
            FakeFeaturesRepository().apply {
                saveTrackFeatures(insertedTracks)
            }
        )
        viewModel.initializeTrackFeatures("id")

        val retrievedFeatures = viewModel.trackFeatures.first()

        assertTrue(retrievedFeatures.size == 1)
    }
}