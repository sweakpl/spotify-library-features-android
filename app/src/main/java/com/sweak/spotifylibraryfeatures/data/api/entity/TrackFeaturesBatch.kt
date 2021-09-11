package com.sweak.spotifylibraryfeatures.data.api.entity

import com.google.gson.annotations.SerializedName
import com.sweak.spotifylibraryfeatures.data.database.entity.TrackFeatures

data class TrackFeaturesBatch(
    @SerializedName("audio_features") val items: List<TrackFeatures>
)
