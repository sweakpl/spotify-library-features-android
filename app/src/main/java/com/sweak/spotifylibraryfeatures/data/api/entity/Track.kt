package com.sweak.spotifylibraryfeatures.data.api.entity

data class Track(
    val id: String,
    val name: String,
    val artists: List<Artist>,
    val album: Album
)
