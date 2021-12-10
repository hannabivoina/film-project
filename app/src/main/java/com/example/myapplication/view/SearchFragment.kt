package com.example.myapplication.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.common.contract
import com.example.myapplication.databinding.FragmentSearchBinding
import com.example.myapplication.model.dataModels.HistoryFilm
import com.example.myapplication.view.adapter.FilmInterface
import com.example.myapplication.view.adapter.HistoryAdapter
import com.example.myapplication.view.adapter.SaveFilmInterface
import com.example.myapplication.view.adapter.SearchAdapter
import com.example.myapplication.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar_search.*

private const val statusSearch = "search"
private const val statusHistory = "history"

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchHistoryAdapter = SearchAdapter(statusHistory, object : FilmInterface {
        override fun goToFilm(id: String) {
            contract()?.toFilmInformation(id)
        }
    }, object : SaveFilmInterface{
        override fun addFilmToSaved(historyFilm: HistoryFilm) {
            viewModel.addHistoryToSaved(historyFilm)
        }
    })

    private val searchNewAdapter = SearchAdapter(statusSearch, object : FilmInterface {
        override fun goToFilm(id: String) {
            contract()?.toFilmInformation(id)
        }
    }, object : SaveFilmInterface{
        override fun addFilmToSaved(historyFilm: HistoryFilm) {
            viewModel.addHistoryToSaved(historyFilm)
        }
    })

    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState==null){
            viewModel.changeFocus(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        if(contract()?.isNetworkAvailable(requireContext()) == true){

            viewModel.getSavedHistoryFilms(statusHistory)

            viewModel.historyLiveData.observe(viewLifecycleOwner) {
                if (it != null) {
                    viewModel.searchInfoForHistoryCards(it)
                    viewModel.nullLiveData()
                }
            }

            viewModel.historyFilmCardsLiveData.observe(viewLifecycleOwner) {
                if (it != null){
                    val adapter = HistoryAdapter(object : FilmInterface {
                        override fun goToFilm(id: String) {
                            contract()?.toFilmInformation(id)
                        }
                    }, it)
                    setupHistoryRecycler(adapter)
                }
            }

            setupSearchHistoryRecycler()
            setupSearchNewRecycler()

            viewModel.searchHistoryLiveData.observe(viewLifecycleOwner) { history ->
                if (history != null) {
                    searchHistoryAdapter.updateList(history)
                }
            }

            viewModel.searchNewLiveData.observe(viewLifecycleOwner) { search ->
                if (search != null) {
                    val searchListWithoutMatches = viewModel.removeMatches(search)
                    searchNewAdapter.updateList(searchListWithoutMatches)
                }
            }

            binding.toolbarSearch.apply {
                toolbarSearchView.setOnClickListener {
                    viewModel.changeFocus(true)
                    viewModel.getSavedHistoryFilms(statusSearch)
                }

                toolbarSearchView.setOnCloseListener {
                    contract()?.toSearchFragment()
                    viewModel.changeFocus(false)
                    return@setOnCloseListener false
                }
            }

            binding.toolbarSearch.toolbarSearchView.setOnQueryTextListener(object :

                androidx.appcompat.widget.SearchView.OnQueryTextListener {

                override fun onQueryTextChange(query: String?): Boolean {
                    viewModel.changeFocus(true)
                    if (query != null) {
                        viewModel.getSearchLikeHistory(query)
                        viewModel.searchFilmByName(query)
                    }

                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        contract()?.toSearchResult(viewModel.findQueryList())
                    } else {
                        viewModel.changeFocus(true)
                        viewModel.getSavedHistoryFilms(statusSearch)
                    }

                    return false
                }
            })

            viewModel.editModeLiveData.observe(viewLifecycleOwner) {
                if (it) {
                    bindWhenEditModeTrue()
                } else {
                    bindWhenEditModeFalse()
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

    private fun bindWhenEditModeTrue() = with(binding) {
        searchHistoryRecycler.isGone = false
        searchNewRecycler.isGone = false
        historySearchesText.isGone = true
        historyRecycler.isGone = true
    }

    private fun bindWhenEditModeFalse() = with(binding) {
        binding.searchHistoryRecycler.isGone = true
        binding.searchNewRecycler.isGone = true
        binding.historySearchesText.isGone = false
        binding.historyRecycler.isGone = false
    }

    private fun setupHistoryRecycler(historyAdapter: HistoryAdapter) {
        binding.historyRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }
    }

    private fun setupSearchHistoryRecycler() {
        binding.searchHistoryRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchHistoryAdapter
        }
    }

    private fun setupSearchNewRecycler() {
        binding.searchNewRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchNewAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

