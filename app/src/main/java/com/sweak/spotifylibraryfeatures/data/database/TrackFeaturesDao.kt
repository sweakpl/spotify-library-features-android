package com.sweak.spotifylibraryfeatures.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sweak.spotifylibraryfeatures.data.database.entity.TrackFeatures
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackFeaturesDao {

    @Query("SELECT * FROM track_features WHERE id = :id")
    fun getTrackFeatures(id: String): Flow<List<TrackFeatures>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackFeatures(trackFeatures: List<TrackFeatures>)

    @Query("DELETE FROM track_features")
    suspend fun deleteAllTrackFeatures()
}