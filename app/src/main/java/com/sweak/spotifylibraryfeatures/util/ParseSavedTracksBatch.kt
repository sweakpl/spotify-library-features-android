package com.sweak.spotifylibraryfeatures.util

import com.sweak.spotifylibraryfeatures.data.database.entity.Track
import com.sweak.spotifylibraryfeatures.data.api.entity.SavedTrackObjectBatch

fun parseSavedTracksBatch(savedTracksBatch: SavedTrackObjectBatch): List<Track> {
    val trackList = mutableListOf<Track>()

    for (trackFromBatch in savedTracksBatch.items) {

        var artists = ""
        for (artist in trackFromBatch.track.artists)
            artists += "${artist.name}, "
        artists = artists.dropLast(2)

        val track = Track(
            trackFromBatch.track.id,
            trackFromBatch.track.name,
            artists,
            trackFromBatch.addedAt.time,
            trackFromBatch.track.album.images[1].url
        )
        trackList.add(track)
    }

    return trackList
}