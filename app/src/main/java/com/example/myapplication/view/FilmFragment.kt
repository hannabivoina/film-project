package com.example.myapplication.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.model.dataModels.FilmInfo
import com.example.myapplication.R
import com.example.myapplication.common.contract
import com.example.myapplication.databinding.FragmentFilmBinding
import com.example.myapplication.view.adapter.FilmInterface
import com.example.myapplication.view.adapter.FilmStandardAdapter
import com.example.myapplication.view.adapter.StarsAdapter
import com.example.myapplication.view.adapter.StarsFilmsInterface
import com.example.myapplication.viewModel.FilmViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_film.*

@AndroidEntryPoint
class FilmFragment : Fragment(R.layout.fragment_film) {

    private var _binding: FragmentFilmBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FilmViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFilmBinding.inflate(inflater, container, false)

        val args = arguments?.getString(KEY_ARGS_FILM_CONST)

        if (contract()?.isNetworkAvailable(requireContext()) == true) {

            if (args == null) {
                AlertDialog
                    .Builder(requireContext())
                    .setTitle("Something is wrong. Please, reload the app")
                    .setNegativeButton("cancel") { dialog, _ ->
                        dialog.cancel()
                    }.show()
            } else {
                viewModel.searchFilmInfo(args)

                viewModel.filmInfoLiveData.observe(viewLifecycleOwner) {
                    bind(it)
                }

                viewModel.errorLiveData().observe(viewLifecycleOwner){
                    if (it){
                        errorData()
                    }
                }

                viewModel.getSavedFilms()

                binding.filmButtonFavorite.setOnClickListener {
                    if (viewModel.isFilmFavorite) {
                        binding.filmButtonFavorite.setImageResource(getFavoriteButton(false))
                        viewModel.changeFavoriteStatus(false)
                    } else {
                        binding.filmButtonFavorite.setImageResource(getFavoriteButton(true))
                        viewModel.changeFavoriteStatus(true)
                    }
                }

                binding.filmPlot.setOnClickListener {
                    binding.filmMotionLay.setTransition(R.id.start, R.id.end)
                    binding.filmMotionLay.transitionToEnd()
                }

                binding.mainToolbar.toolbarMenuLay.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.app_menu_sign_out -> contract()?.signOut()
                    }
                    true
                }
            }
        } else {
            AlertDialog
                .Builder(requireContext())
                .setTitle("Please, check your internet connection")
                .setNegativeButton("cancel") { dialog, _ ->
                    dialog.cancel()
                }.show()
        }

        return binding.root
    }

    private fun bind(film: FilmInfo) {
        binding.apply {
            binding.mainToolbar.toolbarMainTitle.text = film.title
            filmTitle.text = film.title
            filmGenresList.text = film.genres
            filmPlot.text = film.plot
            filmActorsRecycler.adapter = StarsAdapter(film.actorList, object : StarsFilmsInterface {
                override fun toStarFilms(id: String) {
                    contract()?.toActorFilms(id)
                }
            })
            filmOtherInfoYear.text = film.year
            filmOtherTime.text = film.runtimeStr
            filmType.text = film.type
            filmRating.text = film.imDbRating
            filmButtonFavorite.setImageResource(getFavoriteButton(viewModel.isFilmFavorite))
            filmActorsRecycler.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            filmSimilarMoviesRecycler.adapter = FilmStandardAdapter(object : FilmInterface {
                override fun goToFilm(id: String) {
                    contract()?.toFilmInformation(id)
                }
            }, film.similars)
            filmSimilarMoviesRecycler.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            Glide
                .with(requireContext())
                .load(film.poster)
                .centerCrop()
                .into(filmPoster)

        }
    }

    private fun errorData(){
        binding.filmRatingImage.isGone = true
        AlertDialog
            .Builder(requireContext())
            .setMessage("IMDb Api allows make only 100 calls a day. You can text me in Telegram(@annabivoina) and I'll change Api key and you'll continue use this app")
            .setNegativeButton("cancel") { dialog, _ ->
                dialog.cancel()
                contract()?.toStartSwipeFragment()
            }.show()

    }

    private fun getFavoriteButton(boolean: Boolean) =
        when (boolean) {
            true -> R.drawable.ic_favorite
            false -> R.drawable.ic_favorite_border
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}