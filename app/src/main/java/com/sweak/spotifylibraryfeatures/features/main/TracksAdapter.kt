package com.sweak.spotifylibraryfeatures.features.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sweak.spotifylibraryfeatures.R
import com.sweak.spotifylibraryfeatures.data.database.entity.Track
import com.sweak.spotifylibraryfeatures.databinding.TrackItemBinding

class TracksAdapter(
    private var itemClickListener: ItemClickListener
) : ListAdapter<Track, TracksAdapter.TrackViewHolder>(TracksComparator()) {

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

    class TrackViewHolder(val binding: TrackItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track) {
            binding.apply {
                Glide.with(itemView)
                    .load(track.albumCover)
                    .placeholder(R.drawable.ic_album_placeholder)
                    .into(imageViewAlbumCover)

                textViewTrackName.text = track.name
                textViewTrackArtists.text = track.artists
            }
        }
    }

    class TracksComparator : DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Track, newItem: Track) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding =
            TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            with(holder) {
                bind(currentItem)
                binding.root.setOnClickListener {
                    itemClickListener.onItemClick(position)
                }
            }
        }
    }

    fun getTrackAt(position: Int): Track = getItem(position)
}