package com.example.myapplication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.apiResults.randomFilmsResult.Data
import com.example.myapplication.model.dataModels.FilmCard
import com.example.myapplication.model.dataModels.FilmListCategory
import com.example.myapplication.model.ProfileRepositoryImpl
import com.example.myapplication.model.dataModels.FilmCardStandard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor (private val profileRepository: ProfileRepositoryImpl) : ViewModel() {

    private val _savedFilmsLiveData = MutableLiveData<List<FilmCard>?>()
    val savedFilmsLiveData: LiveData<List<FilmCard>?>
        get() = _savedFilmsLiveData

    fun nullLiveData() {
        _savedFilmsLiveData.postValue(null)
    }

    private val _savedSwipesData = SingleLiveEvent<List<FilmCardStandard>>()
    fun getSavedSwipesData(): SingleLiveEvent<List<FilmCardStandard>> {
        return _savedSwipesData
    }


    fun getSavedFilms() {
        viewModelScope.launch {
            _savedFilmsLiveData.postValue(profileRepository.getSavedFavoriteFilms())
        }
    }

    fun getSavedSwipes(){
        viewModelScope.launch {
            _savedSwipesData.postValue(profileRepository.getSavedSwipeFilms())
        }
    }

}