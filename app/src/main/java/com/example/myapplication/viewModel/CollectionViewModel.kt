package com.example.myapplication.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.FilmsRepositoryImpl
import com.example.myapplication.model.dataModels.FilmCardStandard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DISNEY_CONST = "Disney"
private const val ASIAN_CONST = "Asian"
private const val WARNER_CONST = "Warner Bros."
private const val MOST_POPULAR_CONST = "Most Popular"

private const val DISNEY_QUERY = "co0721120"
private const val ASIAN_QUERY = "ls004285275"
private const val WARNER_QUERY = "co000266"

@HiltViewModel
class CollectionViewModel @Inject constructor(private val filmsRepositoryImpl: FilmsRepositoryImpl) : ViewModel() {

    private val _errorCodes = SingleLiveEvent<String>()
    fun getErrorData(): SingleLiveEvent<String> {
        return _errorCodes
    }

    private val _collectionFilmsData = SingleLiveEvent<List<FilmCardStandard>>()
    fun getCollectionFilmsData(): SingleLiveEvent<List<FilmCardStandard>> {
        return _collectionFilmsData
    }

    fun findCollectionFilms(query: String) {
        when (query) {
            DISNEY_CONST -> getCompanyFilms(DISNEY_QUERY)
            ASIAN_CONST -> getListFilms(ASIAN_QUERY)
            WARNER_CONST -> getCompanyFilms(WARNER_QUERY)
            MOST_POPULAR_CONST -> getMostPopular()
        }
    }

    private fun getCompanyFilms(query: String){
        viewModelScope.launch {
            val filmRes = filmsRepositoryImpl.findCompanyFilms(query)
            if (filmRes.isSuccessful) {
                val data = filmRes.body()?.items
                val dataList = ArrayList<FilmCardStandard>()
                if (!data.isNullOrEmpty()){
                    for (i in data){
                        dataList.add(i.toFilmCardStandard())
                    }}
                _collectionFilmsData.postValue(dataList)

            } else {
                _errorCodes.postValue(
                    filmRes.code().toString()
                )
            }
        }
    }

    private fun getListFilms(query: String){
        viewModelScope.launch {
            val filmRes = filmsRepositoryImpl.findImdbListFilms(query)
            if (filmRes.isSuccessful) {
                val data = filmRes.body()?.items
                val dataList = ArrayList<FilmCardStandard>()
                if (!data.isNullOrEmpty()){
                    for (i in data.subList(0,30)){
                        dataList.add(i.toFilmCardStandard())
                    }}
                _collectionFilmsData.postValue(dataList)

            } else {
                _errorCodes.postValue(
                    filmRes.code().toString()
                )
            }
        }
    }

    private fun getMostPopular(){
        viewModelScope.launch {
            val filmRes = filmsRepositoryImpl.findMostPopularFilms()
            if (filmRes.isSuccessful) {
                val data = filmRes.body()?.items
                val dataList = ArrayList<FilmCardStandard>()
                if (!data.isNullOrEmpty()){
                    for (i in data){
                        dataList.add(i.toFilmCardStandard())
                    }}
                _collectionFilmsData.postValue(dataList)

            } else {
                _errorCodes.postValue(
                    filmRes.code().toString()
                )
            }
        }
    }
}