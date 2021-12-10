package com.example.myapplication.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.FilmsRepositoryImpl
import com.example.myapplication.model.SearchRepositoryImpl
import com.example.myapplication.model.dataModels.FilmInfo
import com.example.myapplication.model.dataModels.HistoryFilmCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    private val searchRepository: SearchRepositoryImpl,
    private val filmsRepository: FilmsRepositoryImpl
) : ViewModel() {

    private val _othersFilmInfo = SingleLiveEvent<ArrayList<HistoryFilmCard>>()
    fun getOthersFilmInfo(): SingleLiveEvent<ArrayList<HistoryFilmCard>> {
        return _othersFilmInfo
    }

    fun getInfoForSearchResultList(resultList: ArrayList<String>) {
        if (resultList.isNotEmpty()) {
            getInfoOthersResult(resultList)
        }
    }

    private fun getInfoOthersResult(titlesList: ArrayList<String>) {
        viewModelScope.launch {
            val othersFilmList = ArrayList<HistoryFilmCard>()
            for (i in 0 until titlesList.size) {
                val result = searchRepository.findHistoryFilmCard(titlesList[i])
                if (result.isSuccess) {
                    result.getOrNull()?.let {
                        othersFilmList.add(it)
                    }
                }
            }
            _othersFilmInfo.postValue(othersFilmList)
        }
    }

}