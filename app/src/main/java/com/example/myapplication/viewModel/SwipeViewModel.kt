package com.example.myapplication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.apiResults.randomFilmsResult.Data
import com.example.myapplication.model.FilmsRepositoryImpl
import com.example.myapplication.model.FirebaseRepositoryImpl
import com.example.myapplication.model.dataModels.FilmInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SwipeViewModel @Inject constructor(
    private val firebaseRepositoryImpl: FirebaseRepositoryImpl,
    private val filmsRepository: FilmsRepositoryImpl
) : ViewModel() {

    val randomListTitle = ArrayList<String>()
    val randomListImage = ArrayList<String>()


    private val _errorCodes = SingleLiveEvent<String>()
    fun getErrorData(): SingleLiveEvent<String> {
        return _errorCodes
    }

    private val _randomFilmsData = SingleLiveEvent<List<Data>>()
    fun getRandomFilmsData(): SingleLiveEvent<List<Data>> {
        return _randomFilmsData
    }

    fun gerRandomFilmsForMatching() {
        viewModelScope.launch {
            val resRandomFilms = firebaseRepositoryImpl.findRandomFilmList()
            if (resRandomFilms.isSuccessful) {
                val res = resRandomFilms.body()?.data
                _randomFilmsData.postValue(resRandomFilms.body()?.data)
                shareRandomData(resRandomFilms.body()?.data ?: emptyList())
            } else {
                _errorCodes.postValue(
                    resRandomFilms.code().toString()
                )
            }
        }
    }

    private val _filmInfoLiveData = MutableLiveData<FilmInfo>()
    val filmInfoLiveData: LiveData<FilmInfo>
        get() = _filmInfoLiveData

    fun searchFilmInfo(id: String) {
        viewModelScope.launch {
            val filmsResult = filmsRepository.findFilmInfo(id)
            if (filmsResult.isSuccess) {
                filmsResult.getOrNull()?.let {
                    _filmInfoLiveData.postValue(it)
                }
            }
        }
    }

    private fun shareRandomData(list: List<Data>) {
        if (list.isNotEmpty()) {
            for (i in list) {
                randomListTitle.add(i.id)
                randomListImage.add(i.image)
            }
        }
    }
}