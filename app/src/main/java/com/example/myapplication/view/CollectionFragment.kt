package com.example.myapplication.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.common.contract
import com.example.myapplication.databinding.FragmentCollectionBinding
import com.example.myapplication.model.dataModels.FilmCard
import com.example.myapplication.model.dataModels.FilmCardStandard
import com.example.myapplication.view.adapter.FilmInterface
import com.example.myapplication.view.adapter.FilmListAdapter
import com.example.myapplication.view.adapter.FilmStandardAdapter
import com.example.myapplication.viewModel.CollectionViewModel
import com.example.myapplication.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionFragment : Fragment() {
    private var _binding: FragmentCollectionBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CollectionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCollectionBinding.inflate(inflater, container, false)

        val args = arguments?.getString(KEY_ARGS_COLLECTION_CONST)

        if (contract()?.isNetworkAvailable(requireContext()) == true) {

            if (args != null){

                binding.mainToolbar.toolbarMainTitle.text = args

                viewModel.findCollectionFilms(args)

                viewModel.getCollectionFilmsData().observe(viewLifecycleOwner){
                    setupRecycler(it)
                }

                viewModel.getErrorData().observe(viewLifecycleOwner){

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

    private fun setupRecycler(listFilmCard: List<FilmCardStandard>) {
        binding.collectionFilmsRecycler.apply {
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