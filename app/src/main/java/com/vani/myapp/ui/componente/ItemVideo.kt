package com.vani.myapp.ui.componente

import android.graphics.Color
import android.view.View
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.common.Player
import androidx.media3.ui.R


@OptIn(UnstableApi::class)
@Composable
fun ItemVideo(url: String) {
    val contexto = LocalContext.current
    var cargandoVideo by remember { mutableStateOf(true) }

    val exoPlayer = remember(url) {
        ExoPlayer.Builder(contexto).build().apply {
            setMediaItem(MediaItem.fromUri(url))
            prepare()
            volume = 0f
            repeatMode = ExoPlayer.REPEAT_MODE_ONE
            playWhenReady = true

            // Escuchamos si hay errores o si ya cargó
            addListener(object : Player.Listener {
                // Es obligatorio poner override fun y el nombre exacto
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_READY) {
                        cargandoVideo = false
                    }
                }
            })
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    // Forzamos TextureView para evitar pantallas negras en Xiaomi
                    val surface = this.rootView.findViewById<View>(R.id.exo_content_frame)
                    // (Asegúrate de que el fondo sea negro mientras carga)
                    setBackgroundColor(Color.BLACK)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        if (cargandoVideo) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
        }
    }

    DisposableEffect(url) {
        onDispose { exoPlayer.release() }
    }
}