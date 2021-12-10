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

    private val _errorCodes = SingleLiveEvent<String>()
    fun getErrorData(): SingleLiveEvent<String> {
        return _errorCodes
    }

    private val _filmInfoData = SingleLiveEvent<FilmInfo>()
    fun getFilmData(): SingleLiveEvent<FilmInfo> {
        return _filmInfoData
    }

    private val stream = MutableLiveData<SwipeRightModel>()
    val modelStream: LiveData<SwipeRightModel>
        get() = stream

    private val data = arrayListOf<SwipeRightCardModel>(
        SwipeRightCardModel(
            filmId = "tt10160804",
            filmImage = "https://imdb-api.com/images/original/MV5BNmQ1MGQ2NjItNzVmOC00MmIwLWJjZTUtNGFlMmNjYWE2NjNkXkEyXkFqcGdeQXVyNjY1MTg4Mzc@._V1_Ratio0.8043_AL_.jpg",
            filmTitle = "Hawkeye",
            filmFullTitle = "Hawkeye (2021)",
            filmYear = "2021",
            filmGenreList = "Action, Adventure, Crime",
            filmPlot = "Former Avenger Clint Barton has a seemingly simple mission: get back to his family for Christmas. Possible? Maybe with the help of Kate Bishop, a 22-year-old archer with dreams of becoming a Super Hero. The two are forced to work together when a presence from Barton's past threatens to derail far more than the festive spirit."
        ),
        SwipeRightCardModel(
            filmId = "tt4236770",
            filmTitle = "Yellowstone",
            filmImage = "https://imdb-api.com/images/original/MV5BZjhkNWM1NTQtODI4NS00NzllLTg1ODAtYjg3N2QxZjc3ZjRiXkEyXkFqcGdeQXVyNDIzMzcwNjc@._V1_Ratio0.7388_AL_.jpg",
            filmFullTitle = "Yellowstone (2018– )",
            filmYear = "2018",
            filmGenreList = "Drama, Western",
            filmPlot = "Yellowstone follows the Dutton family, led by John Dutton, who controls the largest contiguous ranch in the United States, under constant attack by those it borders - land developers, an Indian reservation, and America's first National Park. It is an intense study of a violent world far from media scrutiny - where land grabs make developers billions, and politicians are bought and sold by the world's largest oil and lumber corporations. Where drinking water poisoned by fracking wells and unsolved murders are not news: they are a consequence of living in the new frontier. It is the best and worst of America seen through the eyes of a family that represents both."
        )
    )

    private var currentIndex = 0

    private val topCard
        get() = data[currentIndex % data.size]
    private val bottomCard
        get() = data[(currentIndex + 1) % data.size]

    init {
        updateStream()
    }

    fun swipe() {
        currentIndex += 1
        updateStream()
    }

    private fun updateStream() {
        stream.value = SwipeRightModel(
            top = topCard,
            bottom = bottomCard
        )
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
                }
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