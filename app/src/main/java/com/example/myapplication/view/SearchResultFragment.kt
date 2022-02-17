package com.example.myapplication.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.common.contract
import com.example.myapplication.databinding.FragmentSearchResultBinding
import com.example.myapplication.model.dataModels.FilmInfo
import com.example.myapplication.view.adapter.FilmInterface
import com.example.myapplication.view.adapter.HistoryAdapter
import com.example.myapplication.viewModel.SearchResultViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultFragment : Fragment() {
    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SearchResultViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)

        val args = arguments?.getStringArrayList(KEY_ARGS_SEARCH_CONST)

        if (contract()?.isNetworkAvailable(requireContext()) == true){

            binding.mainToolbar.toolbarMainTitle.text = resources.getString(R.string.search)

            binding.mainToolbar.toolbarMenuLay.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.app_menu_sign_out -> contract()?.signOut()
                }
                true
            }

            if (args != null) {
                viewModel.getInfoForSearchResultList(args)
                binding.searchProgressBar.visibility = ProgressBar.VISIBLE
            }

            viewModel.getOthersFilmInfo().observe(viewLifecycleOwner) {
                if (it.isEmpty()) {
                    binding.searchProgressBar.visibility = ProgressBar.INVISIBLE
                    binding.searchEmpty.isGone = false
                } else {
                    binding.searchProgressBar.visibility = ProgressBar.INVISIBLE
                    val adapter = HistoryAdapter(object : FilmInterface {
                        override fun goToFilm(id: String) {
                            contract()?.toFilmInformation(id)
                        }
                    }, it)
                    setupOthersRecycler(adapter)
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

    private fun setupOthersRecycler(othersAdapter: HistoryAdapter) {
        binding.searchResOtherFilmsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = othersAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}