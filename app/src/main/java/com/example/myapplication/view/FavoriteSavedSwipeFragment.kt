package com.example.myapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.common.contract
import com.example.myapplication.databinding.FragmentFavoriteFilmsBinding
import com.example.myapplication.databinding.FragmentFavoriteSwipeBinding
import com.example.myapplication.model.dataModels.FilmCardStandard
import com.example.myapplication.view.adapter.FilmInterface
import com.example.myapplication.view.adapter.FilmStandardAdapter
import com.example.myapplication.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteSavedSwipeFragment: Fragment() {
    private var _binding: FragmentFavoriteSwipeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.getSavedSwipes()

        _binding = FragmentFavoriteSwipeBinding.inflate(inflater, container, false)

        viewModel.getSavedSwipesData().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()){
                setupRecycler(it)
            }
        }
        
        return binding.root
    }

    private fun setupRecycler(listFilmCard: List<FilmCardStandard>) {
        binding.favoriteSwipeFilmsRecycler.isNestedScrollingEnabled = true

        binding.favoriteSwipeFilmsRecycler.apply {
            adapter = FilmStandardAdapter(object : FilmInterface {
                override fun goToFilm(id: String) {
                    contract()?.toFilmInformation(id)
                }
            }, listFilmCard)
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}