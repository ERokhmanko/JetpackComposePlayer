package ru.netology.player.ui.composable

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import ru.netology.player.MediaLifecycleObserver
import ru.netology.player.R
import ru.netology.player.dto.Album
import ru.netology.player.ui.playNextTrack
import ru.netology.player.ui.playPrevTrack
import ru.netology.player.ui.theme.Purple500
import ru.netology.player.ui.trackCycle
import ru.netology.player.viewmodel.PlayerViewModel


@Composable
fun MainComposable(
   viewModel: PlayerViewModel,
    mediaObserver: MediaLifecycleObserver,
    source: LifecycleOwner,
) {
    var album by remember {
        mutableStateOf(viewModel.data.value)
    }
    viewModel.data.observe(source){
        album = it
    }
    Column(
        Modifier.padding(10.dp)
    ) {

        album?.let { Header(it) }
        Row {
            PlayFloating(viewModel, mediaObserver, source)
            Slider(
                viewModel = viewModel,
                mediaObserver = mediaObserver,
                source = source
            )
        }
        album?.let { ListTracks(mediaObserver, viewModel, source, it) }
    }
}

@Composable
fun ListTracks(
    mediaObserver: MediaLifecycleObserver,
    viewModel: PlayerViewModel,
    source: LifecycleOwner,
    album: Album,

) {
    LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(),
        ) {
            itemsIndexed(items = album.tracks) { _, track ->
                Row(
                    modifier = Modifier.padding(start = 5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.defaultMinSize(40.dp, 40.dp),
                        shape = RoundedCornerShape(30.dp),
                        contentPadding = PaddingValues(0.dp),
                        onClick = {
                            trackCycle(
                                track,
                                mediaObserver = mediaObserver,
                                viewModel = viewModel,
                                source = source
                            )
                        }) {

                        Icon(
                            painter = if (track.running) painterResource(id = R.drawable.ic_baseline_pause_24)
                            else painterResource(id = R.drawable.ic_baseline_play_arrow_24),
                            contentDescription = "Следующий трэк",
                            tint = Color.White
                        )

                    }
                    Text(
                        modifier = Modifier.padding(top = 10.dp, start = 10.dp),
                        text = track.file,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )


                    Text(
                        modifier = Modifier.padding(top = 10.dp, start = 10.dp),
                        text = album.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }



@Composable
fun Header(album: Album) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize(0.25f)
            .padding(10.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp
    ) {
        Column(modifier = Modifier.padding(start = 15.dp, top = 10.dp)) {
            Text(
                text = stringResource(R.string.title),
                color = Color.Gray,
                fontFamily = FontFamily.Monospace,
                fontSize = 20.sp
            )

                Text(
                    text = album.title,
                    color = Color.Black,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 20.sp
                )


                Row {
                    Text(text = stringResource(R.string.artist))
                    Text(
                        modifier = Modifier.padding(start = 15.dp),
                        text = album.artist,
                        color = Color.Black,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 18.sp
                    )

                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(text = album.published)
                    Text(modifier = Modifier.padding(start = 15.dp), text = album.genre)

                }
            }
        }
    }



@Composable
fun PlayFloating(
    viewModel: PlayerViewModel,
    mediaObserver: MediaLifecycleObserver,
    source: LifecycleOwner
) {
    FloatingActionButton(
        backgroundColor = Purple500,
        modifier = Modifier.padding(end = 5.dp),
        onClick = {
            if (viewModel.trackPosn.value == null) {
                val firstTrack = viewModel.getFirstTrack()
                if (firstTrack != null) {
                    trackCycle(
                        track = firstTrack,
                        mediaObserver = mediaObserver,
                        viewModel = viewModel,
                        source = source
                    )
                }
            } else {
                val trackList = viewModel.getTrackList()
                trackList?.forEach {
                    if (it.id == viewModel.trackPosn.value) {
                        val track = it
                        trackCycle(
                            track = track,
                            mediaObserver = mediaObserver,
                            viewModel = viewModel,
                            source = source
                        )
                    }
                }
            }
        }
    ) {

        Icon(
            painter = if (mediaObserver.getPlaying() == false) painterResource(id = R.drawable.ic_baseline_play_arrow_24)
            else painterResource(id = R.drawable.ic_baseline_pause_24),
            contentDescription = "Запуск и пауза песни",
            tint = Color.Black
        )

    }
}

@Composable
fun Slider(
    viewModel: PlayerViewModel,
    mediaObserver: MediaLifecycleObserver,
    source: LifecycleOwner
) {
    var sliderPosition by remember { mutableStateOf(0F) }
    Row(
        modifier = Modifier.alpha(if (viewModel.getIsSeekBar() == true) 1f else 0f)
    ) {
        Button(
            modifier = Modifier.defaultMinSize(48.dp, 48.dp),
            colors = ButtonDefaults.outlinedButtonColors(),
            onClick = { playPrevTrack(viewModel, mediaObserver, source) }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_skip_previous_24),
                contentDescription = "Предыдущий трэк",
                tint = Purple500
            )

        }

        Button(
            modifier = Modifier.defaultMinSize(48.dp, 48.dp),
            colors = ButtonDefaults.outlinedButtonColors(),
            onClick = { playNextTrack(viewModel, mediaObserver, source) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_skip_next_24),
                contentDescription = "Следующий трэк",
                tint = Purple500
            )

        }

        val maxWidth = mediaObserver.getDuration()?.toFloat() ?: 0F

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {

            override fun run() {
                try {
                    sliderPosition = mediaObserver.getCurrentPosition()?.toFloat() ?: 0F
                    handler.postDelayed(this, 1000)

                } catch (e: Exception) {

                    sliderPosition = 0F
                }
            }
        }, 0)

        Slider(
            value = sliderPosition,
            onValueChange = { valueChange ->
                sliderPosition = valueChange
            },
            valueRange = 0f..maxWidth,
            onValueChangeFinished = {
                mediaObserver.player?.seekTo(sliderPosition.toInt())
            },
            colors = SliderDefaults.colors(
                thumbColor = Purple500,
                activeTrackColor = Purple500
            ),
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}






