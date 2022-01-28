package com.example.myapplication.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.viewModel.ActorFilmsViewModel
import com.example.myapplication.R
import com.example.myapplication.model.dataModels.ActorInfo
import com.example.myapplication.common.contract
import com.example.myapplication.databinding.FragmentActorBinding
import com.example.myapplication.model.dataModels.FilmCardStandard
import com.example.myapplication.view.adapter.FilmInterface
import com.example.myapplication.view.adapter.FilmStandardAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ActorFragment : Fragment() {

    private var _binding: FragmentActorBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ActorFilmsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActorBinding.inflate(inflater, container, false)

        val args = arguments?.getString(KEY_ARGS_ACTOR_CONST)

        if(contract()?.isNetworkAvailable(requireContext()) == true){

            if (args != null) {
                viewModel.searchActorsFilm(args)

                viewModel.actorFilmsLiveData.observe(viewLifecycleOwner) { info ->
                    binding.mainToolbar.toolbarMainTitle.text = info.name
                    setupRecycler(info.knownFor.map { it.toFilmCardStandard() })
                    bindActorInfo(info)
                }
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

    private fun bindActorInfo(actInfo: ActorInfo) {
        binding.apply {
            actorName.text = actInfo.name
            actorJob.text = actInfo.role
            actorInfo.text = actInfo.summary
            actorBirth.text = actInfo.birthDate

            Glide
                .with(requireContext())
                .load(actInfo.image)
                .centerCrop()
                .into(actorPhoto)
        }
    }

    private fun setupRecycler(listFilmCard: List<FilmCardStandard>) {
        binding.recyclerActorFilms.apply {
            adapter = FilmStandardAdapter(object : FilmInterface {
                override fun goToFilm(id: String) {
                    contract()?.toFilmInformation(id)
                }
            }, listFilmCard)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}