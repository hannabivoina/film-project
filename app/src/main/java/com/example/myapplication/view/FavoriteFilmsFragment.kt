package com.example.myapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.model.dataModels.FilmCard
import com.example.myapplication.R
import com.example.myapplication.common.contract
import com.example.myapplication.databinding.FragmentActorBinding
import com.example.myapplication.databinding.FragmentFavoriteBinding
import com.example.myapplication.databinding.FragmentFavoriteFilmsBinding
import com.example.myapplication.view.adapter.FilmInterface
import com.example.myapplication.view.adapter.FilmListAdapter
import com.example.myapplication.viewModel.ProfileViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFilmsFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(layoutInflater)

        viewPager = binding.pager

        val pagerAdapter = ScreenSlidePagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapter

        tabLayout = binding.tabLayout

        TabLayoutMediator(tabLayout, viewPager){tab, position ->

        }.attach()


        binding.mainToolbar.apply {
            toolbarMainTitle.text = resources.getString(R.string.favorite)
            toolbarMenuLay.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.app_menu_sign_out -> contract()?.signOut()
                }
                true
            }
        }

        onBackPressed()

        return binding.root
    }

    private fun onBackPressed() {
        if (viewPager.currentItem != 0) {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }


    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        val fragmentsList =
            listOf<Fragment>(FavoriteSavedFilmsFragment(), FavoriteSavedSwipeFragment())

        override fun getItemCount(): Int = fragmentsList.size

        override fun createFragment(position: Int): Fragment = fragmentsList[position]
    }
}