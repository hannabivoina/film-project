package com.example.myapplication.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentTrailerViewBinding
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ui.PlayerView
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util

@AndroidEntryPoint
class TrailerViewFragment : Fragment() {
    private var _binding: FragmentTrailerViewBinding? = null
    private val binding get() = _binding!!
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    private val videoURL =
        "https://r2---sn-4g5lznl7.googlevideo.com/videoplayback?expire=1638747071&ei=X_esYbTzJYWh1wae3KLQBQ&ip=144.76.178.200&id=o-ALXvCWL11XWwMQo0bs6jbK2c0VrGvR4YLZCkTGkcvkyw&itag=18&source=youtube&requiressl=yes&mh=1C&mm=31%2C29&mn=sn-4g5lznl7%2Csn-4g5edn6r&ms=au%2Crdu&mv=u&mvi=2&pl=24&vprv=1&mime=video%2Fmp4&ns=QjYHWuqOaq7oheG3hD44hGIG&gir=yes&clen=5016748&ratebypass=yes&dur=83.150&lmt=1416917420476214&mt=1638724760&fvip=2&fexp=24001373%2C24007246&c=WEB&n=c_uVPNR9qp0Fvo1h3s&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cvprv%2Cmime%2Cns%2Cgir%2Cclen%2Cratebypass%2Cdur%2Clmt&sig=AOq0QJ8wRgIhALy6AoECNiUMXau1ratJu0MME2PkOZQqXNWg8e9edvL7AiEAzRL43l5hvaqki0TM71G0488GoJy2b61tWnxozOZWvRI%3D&lsparams=mh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl&lsig=AG3C_xAwRAIgfadda8T1cuswjy2I7gjsTZdNRyvH3fwe_fF4gTxLEj8CIAkLeDDrqgk6aG-tEaixptHPxVIvRXODv70b8QUwSyK5&title=Inception+-+Official+Trailer+[HD]"
    private lateinit var exoPlayerView: PlayerView
    private var exoPlayer: ExoPlayer? = null
    private lateinit var mediaSource: MediaSource
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrailerViewBinding.inflate(inflater, container, false)

        initPlayer()

        exoPlayer = ExoPlayer.Builder(requireContext()).build()

        return binding.root
    }

    private fun initPlayer(){
        exoPlayerView = binding.videoTrailerView

        exoPlayer = ExoPlayer.Builder(requireContext()).build().also { it ->
            exoPlayerView.player = it
            val mediaItem = MediaItem.fromUri(videoURL)
            it.setMediaItem(mediaItem)
        }
        exoPlayer?.playWhenReady = playWhenReady
        exoPlayer?.seekTo(currentWindow, playbackPosition)
        exoPlayer?.prepare()
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if ((Util.SDK_INT < 24 || exoPlayer == null)) {
            releasePlayer()
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        binding.videoTrailerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }


    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }}

    private fun releasePlayer() {
        exoPlayer = null
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}