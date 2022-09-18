package ru.netology.player.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.coroutineScope
import ru.netology.player.R
import ru.netology.player.databinding.CardTrackBinding
import ru.netology.player.dto.Album
import ru.netology.player.dto.Track

interface TrackCallback {
    fun play(track: Track)

}

class TrackAdapter(private val trackCallback: TrackCallback) :
    ListAdapter<Track, TrackViewHolder>(TrackDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding =
            CardTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding, trackCallback)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = getItem(position)
        holder.bind(track)
    }

}

class TrackViewHolder(
    private val binding: CardTrackBinding,
    private val trackCallback: TrackCallback
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {

        with(binding) {
            nameTrack.text = track.file
            playPause.isChecked = track.running
            nameAlbum.text = track.titleAlbum

            playPause.setOnClickListener {
                trackCallback.play(track)
            }
        }

    }
}

class TrackDiffCallback : DiffUtil.ItemCallback<Track>() {

    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem == newItem
    }

}