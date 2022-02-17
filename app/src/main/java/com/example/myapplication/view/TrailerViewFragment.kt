package com.example.myapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentTrailerViewBinding
import com.google.android.exoplayer2.*
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.exoplayer2.ExoPlayer


@AndroidEntryPoint
class TrailerViewFragment : Fragment() {

    private var _binding: FragmentTrailerViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var exoPlayer: ExoPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrailerViewBinding.inflate(inflater, container, false)

        val args = arguments?.getString(KEY_ARGS_TRAILER_CONST)

        exoPlayer = ExoPlayer.Builder(requireContext()).build()

        binding.trailerPlayerView.player = exoPlayer

        initPlayer(args)

        return binding.root
    }

    private fun initPlayer(url: String?) {
        if (url != null) {
            val mediaItem = MediaItem.fromUri(url)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
        }
    }

    override fun onPause() {
        super.onPause()
        exoPlayer.stop()
    }

    override fun onStop() {
        super.onStop()

        exoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}