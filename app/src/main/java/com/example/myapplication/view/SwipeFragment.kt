package com.example.myapplication.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.SwipeRightViewModel
import com.example.myapplication.common.contract
import com.example.myapplication.databinding.FragmentSwipeBinding
import com.example.myapplication.model.dataModels.FilmInfo
import com.example.myapplication.model.dataModels.swipeModel.SwipeRightModel
import com.example.myapplication.view.registrationView.StartActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SwipeFragment : Fragment() {

    private var _binding: FragmentSwipeBinding? = null
    private val binding get() = _binding!!

    private val viewModelSwipe by viewModels<SwipeRightViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSwipeBinding.inflate(inflater, container, false)

        viewModelSwipe.gerRandomFilmsForMatching()

        viewModelSwipe.getRandomFilmsData().observe(viewLifecycleOwner) {

            viewModelSwipe
                .modelStream
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    bindCard(it)
                })

            binding.motionLayout.setTransitionListener(object : TransitionAdapter() {

                override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                    when (currentId) {
                        R.id.offScreenPass -> {
                            motionLayout.progress = 0f
                            motionLayout.setTransition(R.id.rest, R.id.pass)
                            viewModelSwipe.swipe()
                        }
                        R.id.offScreenLike -> {
                            motionLayout.progress = 0f
                            motionLayout.setTransition(R.id.rest, R.id.like)
                            viewModelSwipe.swipe()
                            viewModelSwipe.addSwipeToSaved()
                        }
                    }
                }
            })

            binding.topCardCloseBut.setOnClickListener {
                AlertDialog
                    .Builder(requireContext())
                    .setTitle("Do you want to finish this session?")
                    .setPositiveButton("yes") { _, _ ->
                        toMainActivity()}
                    .setNegativeButton("cancel") { dialog, _ ->
                        dialog.cancel()
                    }.show()
            }
        }

        viewModelSwipe.getErrorApiCalls().observe(viewLifecycleOwner){
            errorFromApiCalls()
        }

        return binding.root
    }

    private fun bindCard(model: SwipeRightModel) {
        binding.apply {
            filmMatchFilmTitle.text = model.top.filmFullTitle
            filmMatchGenresList.text = model.top.filmGenreList
            filmMatchPlot.text = model.top.filmPlot

            Glide
                .with(requireContext())
                .load(model.top.filmImage)
                .placeholder(R.drawable.ic_default_film_image_foreground)
                .optionalCenterCrop()
                .into(topCardImage)

            Glide
                .with(requireContext())
                .load(model.bottom.filmImage)
                .optionalCenterCrop()
                .into(bottomCardImage)

        }
    }

    private fun toMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

    private fun errorFromApiCalls(){
        AlertDialog
            .Builder(requireContext())
            .setMessage("IMDb Api allows make only 100 calls a day. You can text me in Telegram(@annabivoina) and I'll change Api key and you'll continue use this app")
            .setNegativeButton("cancel") { dialog, _ ->
                dialog.cancel()
            }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
