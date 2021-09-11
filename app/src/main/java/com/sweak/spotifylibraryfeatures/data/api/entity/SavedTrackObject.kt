package com.sweak.spotifylibraryfeatures.data.api.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class SavedTrackObject(
    @SerializedName("added_at") val addedAt: Date,
    val track: Track
)
