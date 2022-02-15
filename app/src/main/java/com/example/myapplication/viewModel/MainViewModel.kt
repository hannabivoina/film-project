package com.example.myapplication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.dataModels.FilmListCategory
import com.example.myapplication.model.FilmsRepositoryImpl
import com.example.myapplication.model.dataModels.CategoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject


private const val DISNEY_CONST = "Disney"
private const val ASIAN_CONST = "Asian"
private const val WARNER_CONST = "Warner Bros."
private const val MOST_POPULAR_CONST = "Most Popular"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val filmsRepository: FilmsRepositoryImpl,
) : ViewModel() {

    val newArr = ArrayList<FilmListCategory>()
    val listCollection = listOf<String>(DISNEY_CONST, ASIAN_CONST, WARNER_CONST, MOST_POPULAR_CONST)

    private val _categoriesLiveData = SingleLiveEvent<List<FilmListCategory>>()
    fun getCategoriesData(): SingleLiveEvent<List<FilmListCategory>> {
        return _categoriesLiveData
    }

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        _errorLiveData.postValue(t.toString())
    }

    fun searchFilm() {
        viewModelScope.launch(exceptionHandler) {
            val catList = listOf<String>("Top Films", "Coming Soon")
            val films = filmsRepository.getFilmCategoriesList(catList)
            newArr.clear()
            newArr.addAll(films)
            _categoriesLiveData.postValue(films)
        }
    }


}