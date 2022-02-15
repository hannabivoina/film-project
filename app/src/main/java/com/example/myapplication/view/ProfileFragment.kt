package com.example.myapplication.view

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.common.contract
import com.example.myapplication.databinding.FragmentProfileBinding
import com.example.myapplication.view.registrationView.StartActivity
import com.example.myapplication.viewModel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.*

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val permissionsLauncher = registerForActivityResult(RequestPermission(), ::onGotPermissionsResult)

    private lateinit var auth: FirebaseAuth

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.mainToolbar.toolbarMainTitle.text = resources.getString(R.string.profile)

        viewModel.getSavedFilms()

        viewModel.savedFilmsLiveData.observe(viewLifecycleOwner){
            if (it!=null){
                binding.userFavoriteNumber.text = it.size.toString()
            } else {
                binding.userFavoriteNumber.text = "0"
            }
        }

        if (contract()?.isNetworkAvailable(requireContext()) == true) {

            binding.mainToolbar.toolbarMenuLay.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.app_menu_sign_out -> contract()?.signOut()
                }
                true
            }

            auth = Firebase.auth
            if (auth.currentUser != null) {
                setupProfileImage(auth.currentUser?.photoUrl)
                binding.userName.text = auth.currentUser?.email
            }

            binding.profileSignOut.setOnClickListener {
                contract()?.signOut()
            }

        } else {
            AlertDialog
                .Builder(requireContext())
                .setTitle("Please, check your internet connection")
                .setNegativeButton("cancel") { dialog, _ ->
                    dialog.cancel()
                }.show()
        }

        binding.userPhotoButton.setOnClickListener {
            permissionsLauncher.launch(Manifest.permission.CAMERA)
        }

        return binding.root
    }

    private fun setupProfileImage(uri: Uri?) {
        if (uri != null) {
            Glide
                .with(requireContext())
                .load(uri)
                .circleCrop()
                .into(binding.userPhoto)
        }
    }

    private fun onGotPermissionsResult(granted: Boolean){
        if(granted){
            Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


