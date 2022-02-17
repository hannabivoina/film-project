package com.example.myapplication.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.myapplication.R
import com.example.myapplication.common.AppContract
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.view.registrationView.StartActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

const val KEY_ARGS_FILM_CONST = "KEY_ARGS_FILM"
const val KEY_ARGS_ACTOR_CONST = "KEY_ARGS_ACTOR"
const val KEY_ARGS_SEARCH_CONST = "KEY_ARGS_SEARCH"
const val KEY_ARGS_TRAILER_CONST = "KEY_ARGS_TRAILER"
const val KEY_ARGS_COLLECTION_CONST = "KEY_ARGS_COLLECTION"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AppContract {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        auth = Firebase.auth

        if (savedInstanceState == null) {
            if (auth.currentUser != null) {
                toHomeFragment()
            } else {
                signOut()
            }
        }

        binding.bottomNavigationView.background = null
            binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> toHomeFragment()
                R.id.profile -> toProfileFragment()
                R.id.favorite -> toFavoriteFragment()
                R.id.search -> toSearchFragment()
                R.id.match -> toStartSwipeFragment()
            }
            true
        }
    }

    override fun signOut() {
        clearBackStack()
        auth.signOut()
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)

        finish()
    }

    private fun toHomeFragment() {
        clearBackStack()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainLay, MainFragment())
            .commit()
    }

    private fun toProfileFragment() {
        clearBackStack()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainLay, ProfileFragment())
            .commit()
    }

    private fun toFavoriteFragment() {
        clearBackStack()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainLay, FavoriteFilmsFragment())
            .commit()
    }

    override fun toStartSwipeFragment() {
        clearBackStack()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainLay, StartSwipeFragment())
            .commit()
    }

    override fun toSwipingFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainActivityLay, SwipeFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun toSearchFragment() {
        clearBackStack()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainLay, SearchFragment())
            .commit()
    }

    override fun toFilmInformation(id: String) {
        val fragment = FilmFragment()
        val bundle = Bundle()
        bundle.putString(KEY_ARGS_FILM_CONST, id)
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainLay, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun toActorFilms(id: String) {
        val fragment = ActorFragment()
        val bundle = Bundle()
        bundle.putString(KEY_ARGS_ACTOR_CONST, id)
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainLay, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun toSearchResult(filmsTitle: ArrayList<String>) {
        val fragment = SearchResultFragment()
        val bundle = Bundle()
        bundle.putStringArrayList(KEY_ARGS_SEARCH_CONST, filmsTitle)
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainLay, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun toFilmTrailerFragment(url: String) {
        val fragment = TrailerViewFragment()
        val bundle = Bundle()
        bundle.putString(KEY_ARGS_TRAILER_CONST, url)
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.filmTrailerLay, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }

    override fun toCollection(id: String) {
        val fragment = CollectionFragment()
        val bundle = Bundle()
        bundle.putString(KEY_ARGS_COLLECTION_CONST, id)
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainLay, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun clearBackStack() {
        supportFragmentManager.popBackStack(
            null,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    override fun closeTrailerFragment() {
        val fragment = supportFragmentManager.findFragmentByTag(TrailerViewFragment().tag)
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .remove(fragment)
                .commit()
        }
    }
}
