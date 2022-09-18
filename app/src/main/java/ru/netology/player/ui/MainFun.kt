package ru.netology.player.ui

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.os.Build
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import ru.netology.player.BuildConfig
import ru.netology.player.MediaLifecycleObserver
import ru.netology.player.dto.Track
import ru.netology.player.viewmodel.PlayerViewModel

private lateinit var track: Track

@SuppressLint("ObsoleteSdkInt")
fun trackCycle(
    track: Track,
    mediaObserver: MediaLifecycleObserver,
    viewModel: PlayerViewModel,
    source: LifecycleOwner
) {
    Log.d("ellina", mediaObserver.getPlaying().toString())
    mediaObserver.player?.setOnCompletionListener {
        playNextTrack(viewModel, mediaObserver, source)
    }

    if (track.id != viewModel.trackPosn.value) {
        mediaObserver.onStateChanged(source, Lifecycle.Event.ON_STOP)
    }
    if (mediaObserver.getPlaying() == true) {
        mediaObserver.onStateChanged(source, Lifecycle.Event.ON_PAUSE)

        viewModel.imagePlay(track.id)
    } else {
        viewModel.imagePause(track.id)
        if (track.id != viewModel.trackPosn.value) {
            mediaObserver.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    player?.setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                }
                player?.setDataSource("${BuildConfig.BASE_URL}${track.file}")
            }.play()
            viewModel.setIsSeekBar(track.id != viewModel.trackPosn.value)
        } else {
            mediaObserver.player?.start()
        }
    }
    viewModel.play(track.id)
}

fun playNextTrack(
    viewModel: PlayerViewModel,
    mediaObserver: MediaLifecycleObserver,
    source: LifecycleOwner
) {
    viewModel.data.value?.let {
        track = if (viewModel.trackPosn.value?.toInt() == it.tracks.size) {
            it.tracks.first()
        } else {
            it.tracks[viewModel.trackPosn.value!!.toInt()]
        }
    }
    trackCycle(track, mediaObserver, viewModel, source)
}

fun playPrevTrack(
    viewModel: PlayerViewModel,
    mediaObserver: MediaLifecycleObserver,
    source: LifecycleOwner
) {
    viewModel.data.value?.let {
        track = if (viewModel.trackPosn.value?.toInt() == 1) {
            it.tracks.last()
        } else {
            it.tracks[viewModel.trackPosn.value!!.toInt() - 2]
        }
    }
    trackCycle(track, mediaObserver, viewModel, source)
}