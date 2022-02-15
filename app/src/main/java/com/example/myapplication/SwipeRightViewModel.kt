package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.apiResults.randomFilmsResult.Data
import com.example.myapplication.model.FilmsRepositoryImpl
import com.example.myapplication.model.FirebaseRepositoryImpl
import com.example.myapplication.model.ProfileRepositoryImpl
import com.example.myapplication.model.dataModels.FilmCardStandard
import com.example.myapplication.model.dataModels.FilmInfo
import com.example.myapplication.model.dataModels.swipeModel.SwipeRightCardModel
import com.example.myapplication.model.dataModels.swipeModel.SwipeRightModel
import com.example.myapplication.viewModel.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SwipeRightViewModel @Inject constructor(
    private val firebaseRepositoryImpl: FirebaseRepositoryImpl,
    private val filmsRepositoryImpl: FilmsRepositoryImpl,
    private val profileRepositoryImpl: ProfileRepositoryImpl
) : ViewModel() {

    private var apiCalls = true

    private val _errorApiCalls = SingleLiveEvent<Boolean>()
    fun getErrorApiCalls(): SingleLiveEvent<Boolean> {
        return _errorApiCalls
    }

    private val stream = MutableLiveData<SwipeRightModel>()
    val modelStream: LiveData<SwipeRightModel>
        get() = stream

    private val data = arrayListOf<SwipeRightCardModel>()

    private var currentIndex = 0

    private val topCard
        get() = data[currentIndex % data.size]
    private val bottomCard
        get() = data[(currentIndex + 1) % data.size]

    fun swipe() {
        if (data.size != 0) {
            currentIndex += 1
            updateStream()
        }
    }

    private fun updateStream() {
        if (data.size != 0) {
            stream.value = SwipeRightModel(
                top = topCard,
                bottom = bottomCard
            )
        }
    }

    private val _errorCodesRandom = SingleLiveEvent<String>()
    fun getErrorDataRandom(): SingleLiveEvent<String> {
        return _errorCodesRandom
    }

    private val _randomFilmsData = SingleLiveEvent<List<Data>>()
    fun getRandomFilmsData(): SingleLiveEvent<List<Data>> {
        return _randomFilmsData
    }

    fun gerRandomFilmsForMatching() {
        viewModelScope.launch {
            val resRandomFilms = firebaseRepositoryImpl.findRandomFilmList()
            if (resRandomFilms.isSuccessful) {
                val randomData = resRandomFilms.body()?.data
                _randomFilmsData.postValue(resRandomFilms.body()?.data)
                convertRandomDataToSwipeCard(randomData ?: emptyList())
            } else {
                _errorCodesRandom.postValue(
                    resRandomFilms.code().toString()
                )
            }
        }
    }

    private fun findInformationAboutFilm(id: String) {
        viewModelScope.launch {
            val filmRes = filmsRepositoryImpl.findFilmInfo(id)
            if (filmRes.isSuccess) {
                filmRes.getOrNull()?.let {
                    data.add(it.toFilmSwipeStandard())
                    updateStream()
                }
            } else {
                apiCalls = false
            }
        }
    }

    private fun convertRandomDataToSwipeCard(
        list: List<Data>
    ) {
        for (i in list) {
            findInformationAboutFilm(i.id)
        }
    }

    fun addSwipeToSaved() {
        viewModelScope.launch {
            profileRepositoryImpl.addSwipeFilmToSaved(data[currentIndex - 1].toFilmCardStandard())
        }
    }
}