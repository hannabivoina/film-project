package com.example.myapplication.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.common.contract
import com.example.myapplication.databinding.FragmentMainBinding
import com.example.myapplication.view.adapter.CategoriesAdapter
import com.example.myapplication.view.adapter.CollectionInterface
import com.example.myapplication.viewModel.MainViewModel
import com.example.myapplication.view.adapter.FilmInterface
import com.example.myapplication.view.adapter.FilmListCategoryAdapter
import com.example.myapplication.viewModel.ProfileViewModel
import com.example.myapplication.viewModel.ProfileViewModel_Factory
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.CoroutineExceptionHandler

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val categoryAdapter = FilmListCategoryAdapter(object : FilmInterface {
        override fun goToFilm(id: String) {
            contract()?.toFilmInformation(id)
        }
    })

    private val viewModel by viewModels<MainViewModel>()
    private val profileViewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            profileViewModel.getSavedCategories()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        setupCategoryRecyclerView()
        binding.mainToolbar.toolbarMainTitle.text = resources.getString(R.string.films)

        if(contract()?.isNetworkAvailable(requireContext()) == true){

            viewModel.searchFilm()

            viewModel.getCategoriesData().observe(viewLifecycleOwner) {
                if (it != null) {
                    categoryAdapter.updateList(it)
                    setupCollectionRecyclerView(viewModel.listCollection)
                }
            }

            viewModel.errorLiveData.observe(viewLifecycleOwner){
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }

            binding.mainToolbar.toolbarMenuLay.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.app_menu_sign_out -> contract()?.signOut()
                }
                true
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

    private fun setupCollectionRecyclerView(list: List<String>){
        val adapter = CategoriesAdapter(object : CollectionInterface{
            override fun goToCategory(title: String) {
                contract()?.toCollection(title)
            }
        }, list)

        binding.filmsCollectionRecycler.adapter = adapter
        binding.filmsCollectionRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun setupCategoryRecyclerView() {
        binding.apply {
            filmsCategoryLay.layoutManager = LinearLayoutManager(requireContext())
            filmsCategoryLay.adapter = categoryAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}