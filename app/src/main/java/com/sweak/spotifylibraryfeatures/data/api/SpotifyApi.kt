package com.sweak.spotifylibraryfeatures.data.api

import com.sweak.spotifylibraryfeatures.data.api.entity.SavedTrackObjectBatch
import com.sweak.spotifylibraryfeatures.data.api.entity.TrackFeaturesBatch
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyApi {

    companion object {
        const val BASE_URL = "https://api.spotify.com/v1/"
    }

    @GET("me/tracks")
    suspend fun getSavedTracks(
        @Header("Authorization") bearerWithToken: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): SavedTrackObjectBatch

    @GET("audio-features")
    suspend fun getTrackFeatures(
        @Header("Authorization") bearerWithToken: String,
        @Query("ids") ids: String
    ): TrackFeaturesBatch
}