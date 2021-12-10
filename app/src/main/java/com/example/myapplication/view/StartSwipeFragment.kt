package com.example.myapplication.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myapplication.R
import com.example.myapplication.common.contract
import com.example.myapplication.databinding.FragmentStartSwipeBinding
import com.example.myapplication.viewModel.SwipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartSwipeFragment: Fragment() {
    private var _binding: FragmentStartSwipeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SwipeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartSwipeBinding.inflate(inflater, container, false)

        if (contract()?.isNetworkAvailable(requireContext()) == true){
            viewModel.gerRandomFilmsForMatching()

            binding.startSwipeButton.setOnClickListener {
                contract()?.toSwipingFragment()
            }

            binding.mainToolbar.toolbarMainTitle.text = resources.getString(R.string.swipe)

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}