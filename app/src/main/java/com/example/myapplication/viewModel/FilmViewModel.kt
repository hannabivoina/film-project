package com.example.myapplication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.dataModels.FilmCard
import com.example.myapplication.model.dataModels.FilmInfo
import com.example.myapplication.model.FilmsRepositoryImpl
import com.example.myapplication.model.ProfileRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmViewModel @Inject constructor(
    private val filmsRepository: FilmsRepositoryImpl,
    private val profileRepository: ProfileRepositoryImpl
) : ViewModel() {

    var isFilmFavorite = false

    private val _filmInfoLiveData = MutableLiveData<FilmInfo>()
    val filmInfoLiveData: LiveData<FilmInfo> get() = _filmInfoLiveData

    private val _trailerLiveData = SingleLiveEvent<String>()
    fun trailerLiveData(): SingleLiveEvent<String>{
        return _trailerLiveData
    }

    private val _errorLiveData = SingleLiveEvent<Boolean>()
    fun errorLiveData(): SingleLiveEvent<Boolean>{
        return _errorLiveData
    }

    fun searchFilmInfo(id: String) {
        findFilmTrailer(id)
        viewModelScope.launch {
            val filmsResult = filmsRepository.findFilmInfo(id)
            if (filmsResult.isSuccess) {
                filmsResult.getOrNull()?.let {
                    isFilmFavorite = checkIsFilmFavorite(id)
                    _filmInfoLiveData.postValue(it)
                }
            } else {
                _errorLiveData.postValue(true)
            }
        }
    }

    private fun checkIsFilmFavorite(filmId: String): Boolean {
        var check = false
        viewModelScope.async {
            check = profileRepository.findFilmCardWithId(filmId) != null
        }

        return check
    }

    fun changeFavoriteStatus(status: Boolean) {
        viewModelScope.launch {
            isFilmFavorite = when (status) {
                true -> {
                    profileRepository.addFilmToSaved(filmInfoLiveData.value!!.toFilmCard())
                    getSavedFilms()
                    true
                }
                false -> {
                    profileRepository.deleteFilmFromSaved(filmInfoLiveData.value!!.id)
                    getSavedFilms()
                    false
                }
            }
        }
    }

    private fun findFilmTrailer(id: String){
        viewModelScope.launch {
            val trailerResult = filmsRepository.findFilmTrailer(id)
            println("0000000000000000")
            println(trailerResult.toString())

            if (trailerResult?.isSuccess == true){
                trailerResult.getOrNull()?.let {
                    println("0000000000000001111111110")
                    println(it.toString())
                    _trailerLiveData.postValue(it)
                }
            }
        }
    }

    private val _savedFilmsLiveData = MutableLiveData<List<FilmCard>?>()
    val savedFilmsLiveData: LiveData<List<FilmCard>?>
        get() = _savedFilmsLiveData

    fun getSavedFilms() {
        viewModelScope.launch {
            _savedFilmsLiveData.postValue(profileRepository.getSavedFavoriteFilms())
        }
    }


}