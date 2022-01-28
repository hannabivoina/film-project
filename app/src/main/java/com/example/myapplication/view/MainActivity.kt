package com.example.myapplication.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
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
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.mainLay, FavoriteFilmsFragment())
                    .commit()
            } else {signOut()}
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
        auth.signOut()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainActivityLay, RegistrationFragment())
            .commit()
    }

    private fun toHomeFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainLay, MainFragment())
            .commit()
    }

    private fun toProfileFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainLay, ProfileFragment())
            .commit()
    }

    private fun toFavoriteFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainLay, FavoriteFilmsFragment())
            .commit()
    }

    override fun toStartSwipeFragment() {
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

    override fun toFilmTrailerFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.filmPhotoTrailerLay, TrailerViewFragment())
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
}