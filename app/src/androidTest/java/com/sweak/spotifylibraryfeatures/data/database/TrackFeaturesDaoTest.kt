package com.sweak.spotifylibraryfeatures.data.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.sweak.spotifylibraryfeatures.data.database.entity.TrackFeatures
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random.Default.nextDouble

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TrackFeaturesDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: Database
    private lateinit var dao: TrackFeaturesDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            Database::class.java
        ).allowMainThreadQueries().build()

        dao = database.trackFeaturesDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun shouldContainItem_whenItemInserted() = runBlockingTest {
        val id = "id"
        val trackFeaturesInserted = listOf(
                TrackFeatures(
                    id,
                    nextDouble(),
                    nextDouble(),
                    nextDouble(),
                    nextDouble(),
                    nextDouble(),
                    nextDouble(),
                    nextDouble(),
                    nextDouble(),
                    nextDouble()
                )
            )
        dao.insertTrackFeatures(trackFeaturesInserted)

        val trackFeaturesRetrieved = dao.getTrackFeatures(id).first()

        assertTrue(trackFeaturesRetrieved == trackFeaturesInserted)
    }

    @Test
    fun shouldNotContainAnItem_whenDeletedAllItems() = runBlockingTest {
        val id = "id1"
        val trackFeaturesInserted = listOf(
            TrackFeatures(
                id,
                nextDouble(),
                nextDouble(),
                nextDouble(),
                nextDouble(),
                nextDouble(),
                nextDouble(),
                nextDouble(),
                nextDouble(),
                nextDouble()
            )
        )
        dao.insertTrackFeatures(trackFeaturesInserted)
        dao.deleteAllTrackFeatures()

        val trackFeaturesRetrieved = dao.getTrackFeatures(id).first()

        assertFalse(trackFeaturesRetrieved.contains(trackFeaturesInserted[0]))
    }
}