package com.sweak.spotifylibraryfeatures.features.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweak.spotifylibraryfeatures.MainCoroutineRule
import com.sweak.spotifylibraryfeatures.data.database.entity.Track
import com.sweak.spotifylibraryfeatures.data.repository.FakeFeaturesRepository
import com.sweak.spotifylibraryfeatures.data.repository.FakeSavedTracksRepository
import com.sweak.spotifylibraryfeatures.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: MainViewModel
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
    fun shouldReturnListOfTracks_whenOnStart() = runBlockingTest {
        viewModel = MainViewModel(
            FakeSavedTracksRepository().apply {
                setTracks(insertedTracks)
            },
            FakeFeaturesRepository()
        )
        viewModel.onStart()

        val retrievedTracks = viewModel.savedTracks.first()!!.data

        assertTrue(retrievedTracks!! == insertedTracks)
    }

    @Test
    fun shouldReturnListOfTracks_whenOnRefresh() = runBlockingTest {
        viewModel = MainViewModel(
            FakeSavedTracksRepository().apply {
                setTracks(insertedTracks)
            },
            FakeFeaturesRepository()
        )
        viewModel.onRefresh()

        val retrievedTracks = viewModel.savedTracks.first()!!.data

        assertTrue(retrievedTracks!! == insertedTracks)
    }

    @Test
    fun shouldReturnErrorResource_whenOnStartAndNetworkErrorFlag() = runBlockingTest {
        viewModel = MainViewModel(
            FakeSavedTracksRepository().apply {
                setTracks(insertedTracks)
                shouldReturnNetworkError = true
            },
            FakeFeaturesRepository()
        )
        viewModel.onStart()

        assertTrue(viewModel.savedTracks.first() is Resource.Error)
    }

    @Test
    fun shouldReturnErrorResource_whenOnRefreshAndNetworkErrorFlag() = runBlockingTest {
        viewModel = MainViewModel(
            FakeSavedTracksRepository().apply {
                setTracks(insertedTracks)
                shouldReturnNetworkError = true
            },
            FakeFeaturesRepository()
        )
        viewModel.onRefresh()

        assertTrue(viewModel.savedTracks.first() is Resource.Error)
    }

    @Test
    fun shouldNotThrowError_whenSavingTrackFeatures() = runBlockingTest {
        viewModel = MainViewModel(
            FakeSavedTracksRepository().apply {
                setTracks(insertedTracks)
            },
            FakeFeaturesRepository()
        )
        viewModel.onStart()

        viewModel.saveTrackFeatures(viewModel.savedTracks.first()!!.data!!)
    }
}