package com.sweak.spotifylibraryfeatures.data.api.entity

data class SavedTrackObjectBatch(
    val items: List<SavedTrackObject>,
    val next: String?
)
