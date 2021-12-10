package com.example.myapplication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.dataModels.ActorInfo

import com.example.myapplication.model.FilmsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActorFilmsViewModel @Inject constructor(private val actorRepository: FilmsRepositoryImpl) :
    ViewModel() {
    private val _actorFilmsLiveData = MutableLiveData<ActorInfo>()
    val actorFilmsLiveData: LiveData<ActorInfo>
        get() = _actorFilmsLiveData

    private var job: Job? = null

    override fun onCleared() {
        super.onCleared()
        job = null
    }

    fun searchActorsFilm(query: String) {
        job?.cancel()
        job = viewModelScope.launch {
            val actorFilmsResult = actorRepository.findActorsFilms(query)
            if (actorFilmsResult.isSuccess) {
                actorFilmsResult.getOrNull()?.let {
                    _actorFilmsLiveData.postValue(it)
                }
            }
        }
    }
}