package ru.netology.player

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class MediaLifecycleObserver : LifecycleEventObserver {
    var player: MediaPlayer? = MediaPlayer()

    fun play() {
        player?.setOnPreparedListener {
            it.start()
        }
        player?.prepare()
    }


    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                player?.pause()
            }
            Lifecycle.Event.ON_STOP -> {
                player?.stop()
                player?.reset()
            }
            Lifecycle.Event.ON_DESTROY -> source.lifecycle.removeObserver(this)
            else -> Unit
        }
    }

    fun getCurrentPosition(): Int? {
        return player?.currentPosition
    }

    fun getDuration(): Int? {
        return player?.duration
    }

    fun getPlaying(): Boolean? {
        return player?.isPlaying
    }
}
