package com.example.myapplication.common

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.myapplication.apiResults.randomFilmsResult.Data

fun Fragment.contract() : AppContract? = requireActivity() as? AppContract

interface AppContract {
    fun toFilmInformation(id: String)
    fun toStartSwipeFragment()
    fun toSwipingFragment()
    fun toActorFilms(id: String)
    fun toSearchFragment()
    fun toSearchResult(filmsTitle: ArrayList<String>)
    fun toFilmTrailerFragment(url: String)
    fun isNetworkAvailable(context: Context): Boolean
    fun signOut()
    fun toCollection(id: String)
    fun closeTrailerFragment()
}