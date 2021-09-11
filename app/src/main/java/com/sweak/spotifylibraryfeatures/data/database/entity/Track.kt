package com.sweak.spotifylibraryfeatures.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class Track(
    @PrimaryKey val id: String,
    val name: String,
    val artists: String,
    @ColumnInfo(name = "added_at") val addedAt: Long,
    @ColumnInfo(name = "album_cover") val albumCover: String
)
