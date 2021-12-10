package com.example.myapplication.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.common.App
import com.example.myapplication.model.SearchRepository
import com.example.myapplication.model.SearchRepositoryImpl
import com.example.myapplication.model.dataModels.HistoryFilm
import com.example.myapplication.model.dataModels.HistoryFilmCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val statusSearch = "search"
private const val statusHistory = "history"

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepository: SearchRepositoryImpl) : ViewModel() {

    lateinit var historyList: List<HistoryFilm>

    private val _historyListLiveData = MutableLiveData<List<HistoryFilm>?>()
    val historyLiveData: LiveData<List<HistoryFilm>?>
        get() = _historyListLiveData

    private val _historyFilmCardsLiveData = MutableLiveData<List<HistoryFilmCard>?>()
    val historyFilmCardsLiveData: LiveData<List<HistoryFilmCard>?>
        get() = _historyFilmCardsLiveData

    private val _searchHistoryLiveData = MutableLiveData<List<HistoryFilm>?>()
    val searchHistoryLiveData: LiveData<List<HistoryFilm>?>
        get() = _searchHistoryLiveData

    private val _searchNewLiveData = MutableLiveData<List<HistoryFilm>?>()
    val searchNewLiveData: LiveData<List<HistoryFilm>?>
        get() = _searchNewLiveData

    private val _editModeLiveData = MutableLiveData<Boolean>()
    val editModeLiveData: LiveData<Boolean>
        get() = _editModeLiveData

    fun nullLiveData() {
        _historyListLiveData.postValue(null)
    }

    private var searchJob: Job? = null

    override fun onCleared() {
        super.onCleared()
        searchJob = null
    }

    fun getSavedHistoryFilms(status: String) {
        viewModelScope.launch {
            when (status) {
                statusHistory -> {
                    historyList = searchRepository.getAllSavedHistory().reversed()
                    _historyListLiveData.postValue(historyList)
                }
                statusSearch -> _searchHistoryLiveData.postValue(searchRepository.getAllSavedHistory().reversed())
            }
        }
    }

    fun getSearchLikeHistory(query: String) {
        val searchQuery = "%$query%"
        viewModelScope.launch {
            _searchHistoryLiveData.postValue(searchRepository.searchHistoryInDatabase(searchQuery))
        }
    }

    fun addHistoryToSaved(historyFilm: HistoryFilm) {
        if (!historyList.contains(historyFilm)) {
            viewModelScope.launch {
                searchRepository.insertHistoryToSaved(historyFilm)
            }
        }
    }

    fun searchFilmByName(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val result = searchRepository.findFilmByName(query)
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    _searchNewLiveData.postValue(it)
                }
            }
        }
    }

    fun searchInfoForHistoryCards(historyFilm: List<HistoryFilm>) {
        val historyCardsList = ArrayList<HistoryFilmCard>()
        viewModelScope.launch {
            for (i in historyFilm) {
                val result = searchRepository.findHistoryFilmCard(i.id)
                if (result.isSuccess) {
                    result.getOrNull()?.let {
                        historyCardsList.add(it)
                    }
                }
            }

            _historyFilmCardsLiveData.postValue(historyCardsList)
        }
    }

    fun removeMatches(searchList: List<HistoryFilm>): List<HistoryFilm> {
        val newSearchList = ArrayList<HistoryFilm>()
        newSearchList.addAll(searchList)
        for (i in historyList) {
            if (newSearchList.contains(i)) {
                newSearchList.remove(i)
            }
        }

        return newSearchList.toList()
    }

    fun findQueryList(): ArrayList<String>{
        val searchHistory = _searchHistoryLiveData.value?.map {it.id}
        val newSearch = _searchNewLiveData.value?.map { it.id }

        val resultList = ArrayList<String>()

        if (searchHistory != null){
            resultList.addAll(searchHistory)
        }
        if (newSearch != null){
            resultList.addAll(newSearch)
        }

        return resultList
    }

    fun changeFocus(status: Boolean){
        _editModeLiveData.postValue(status)
    }

}