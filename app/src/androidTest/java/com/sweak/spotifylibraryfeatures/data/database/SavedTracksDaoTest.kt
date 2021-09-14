package com.sweak.spotifylibraryfeatures.data.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.sweak.spotifylibraryfeatures.data.database.entity.Track
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class SavedTracksDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: Database
    private lateinit var dao: SavedTracksDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            Database::class.java
        ).allowMainThreadQueries().build()

        dao = database.savedTracksDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun shouldContainItem_whenItemInserted() = runBlockingTest {
        val track = Track("idofthesong", "songname", "artists", 1234567, "albumcoverurl")
        dao.insertTracks(listOf(track))

        val allTracks = dao.getAllTracks().first()

        assertTrue(allTracks.contains(track))
    }

    @Test
    fun shouldBeEmpty_whenDeletedAllItems() = runBlockingTest {
        val trackOne = Track("idofthesong", "songname", "artists", 1234567, "albumcoverurl")
        val trackTwo = Track("id", "song", "artists", 7654321, "url")
        val trackThree = Track("idsong", "name", "artists", 9876543, "coverurl")
        dao.insertTracks(listOf(trackOne, trackTwo, trackThree))
        dao.deleteAllTracks()

        val allTracks = dao.getAllTracks().first()

        assertTrue(allTracks.isEmpty())
    }
}