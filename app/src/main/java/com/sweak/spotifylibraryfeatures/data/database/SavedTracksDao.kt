package com.sweak.spotifylibraryfeatures.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.sweak.spotifylibraryfeatures.data.database.entity.Track
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedTracksDao {

    @Query("SELECT * FROM tracks")
    fun getAllTracks(): Flow<List<Track>>

    @Insert(onConflict = REPLACE)
    suspend fun insertTracks(tracks: List<Track>)

    @Query("DELETE FROM tracks")
    suspend fun deleteAllTracks()
}