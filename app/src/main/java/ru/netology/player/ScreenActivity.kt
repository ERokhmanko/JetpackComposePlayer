package ru.netology.player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import ru.netology.player.ui.composable.MainComposable
import ru.netology.player.ui.composable.Slider
import ru.netology.player.ui.theme.JetpackComposeTheme
import ru.netology.player.viewmodel.PlayerViewModel

class ScreenActivity : ComponentActivity() {

    private val mediaObserver = MediaLifecycleObserver()
    private val viewModel: PlayerViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            setContent {
                JetpackComposeTheme {
                    MainComposable(
                        viewModel = viewModel,
                        mediaObserver = mediaObserver,
                        source = this,
                    )
                }
            }

        lifecycle.addObserver(mediaObserver)
    }

}

