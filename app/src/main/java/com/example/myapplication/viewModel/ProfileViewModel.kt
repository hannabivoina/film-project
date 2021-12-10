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

    val categoriesArray = arrayOf("Top Films", "Coming Soon", "Dramas")
    var categoriesBooleanArray = booleanArrayOf(false, false, false)

    private val _categoriesListLiveData = SingleLiveEvent<List<String>?>()
    fun getCategoriesList(): SingleLiveEvent<List<String>?> {
        return _categoriesListLiveData
    }

    private val _savedFilmsLiveData = MutableLiveData<List<FilmCard>?>()
    val savedFilmsLiveData: LiveData<List<FilmCard>?>
        get() = _savedFilmsLiveData

    fun nullLiveData() {
        _categoriesListLiveData.postValue(null)
        _savedFilmsLiveData.postValue(null)
    }

    private val _savedSwipesData = SingleLiveEvent<List<FilmCardStandard>>()
    fun getSavedSwipesData(): SingleLiveEvent<List<FilmCardStandard>> {
        return _savedSwipesData
    }

    private fun convertCategories(catBooleanList: BooleanArray): List<FilmListCategory> {
        val catList = ArrayList<FilmListCategory>()
        for (i in categoriesArray.indices) {
            if (catBooleanList[i]) {
                catList.add(FilmListCategory(title = categoriesArray[i]))
            }
        }
        return catList
    }

    fun getSavedCategories() {
        viewModelScope.async {
            val savedCatList = profileRepository.getSavedCategories().map { it.title }
            _categoriesListLiveData.postValue(savedCatList)
            categoriesBooleanArray = convertCategoriesToBoolean(savedCatList)
        }
    }

    fun changeSavedCategories(catBoolList: BooleanArray) {
        val catList = convertCategories(catBoolList)
        viewModelScope.launch {
            val newCatList = profileRepository.addCategoriesToSaved(catList).map { it.title }
            _categoriesListLiveData.postValue(newCatList)
        }
    }

    private fun convertCategoriesToBoolean(catListString: List<String>): BooleanArray {
        val catBooleanList = ArrayList<Boolean>()
        for (i in categoriesArray) {
            if (catListString.contains(i)) {
                catBooleanList.add(true)
            } else {
                catBooleanList.add(false)
            }
        }

        return catBooleanList.toBooleanArray()
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