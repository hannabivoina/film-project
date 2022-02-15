package com.example.myapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.common.contract
import com.example.myapplication.databinding.FragmentFavoriteFilmsBinding
import com.example.myapplication.model.dataModels.FilmCard
import com.example.myapplication.view.adapter.FilmInterface
import com.example.myapplication.view.adapter.FilmListAdapter
import com.example.myapplication.view.adapter.FilmStandardAdapter
import com.example.myapplication.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteSavedFilmsFragment: Fragment() {
    private var _binding: FragmentFavoriteFilmsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.getSavedFilms()

        _binding = FragmentFavoriteFilmsBinding.inflate(inflater, container, false)

        viewModel.savedFilmsLiveData.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                setupRecycler(it)
                viewModel.nullLiveData()
            }
        }

        return binding.root
    }

    private fun setupRecycler(listFilmCard: List<FilmCard>) {
        binding.favoriteFilmsRecycler.apply {
            isNestedScrollingEnabled = false
            adapter = FilmStandardAdapter(object : FilmInterface {
                override fun goToFilm(id: String) {
                    contract()?.toFilmInformation(id)
                }
            }, listFilmCard.map { it.toFilmCardStandard() })
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}