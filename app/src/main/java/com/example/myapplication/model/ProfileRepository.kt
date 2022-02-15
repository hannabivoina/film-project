package com.example.myapplication.model

import com.example.myapplication.model.dataModels.FilmCard
import com.example.myapplication.model.dataModels.FilmListCategory
import com.example.myapplication.database.SavedCategoriesDao
import com.example.myapplication.database.SavedFilmsDao
import com.example.myapplication.database.SavedSwipeFilmsDao
import com.example.myapplication.model.dataModels.FilmCardStandard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ProfileRepositoryImpl {
    suspend fun getSavedFavoriteFilms(): List<FilmCard>
    suspend fun addFilmToSaved(filmCard: FilmCard)
    suspend fun deleteFilmFromSaved(filmId: String): List<FilmCard>
    fun findFilmCardWithId(filmId: String): FilmCard?
    suspend fun getSavedSwipeFilms(): List<FilmCardStandard>
    suspend fun addSwipeFilmToSaved(film: FilmCardStandard)
    suspend fun deleteAllFromSwipe()
}

class ProfileRepository @Inject constructor(
    private val savedCategoriesDao: SavedCategoriesDao,
    private val savedFilmsDao: SavedFilmsDao,
    private val savedSwipeFilmsDao: SavedSwipeFilmsDao
) : ProfileRepositoryImpl {

    private var savedFavoriteFilmsList: List<FilmCard> = emptyList()

    override suspend fun getSavedFavoriteFilms(): List<FilmCard> {
        return withContext(Dispatchers.IO) {
            savedFavoriteFilmsList = savedFilmsDao.getAllSavedFilms()
            savedFavoriteFilmsList
        }
    }

    override suspend fun addFilmToSaved(filmCard: FilmCard){
        withContext(Dispatchers.IO) {
            savedFilmsDao.insertFilmToFavorite(filmCard)
        }
    }

    override suspend fun deleteFilmFromSaved(filmId: String): List<FilmCard> {
        withContext(Dispatchers.IO) {
            savedFilmsDao.deleteFilmFromFavorite(filmId)
            val index = findFilmCardWithId(filmId)
            if (index != null) {
                savedFavoriteFilmsList.toMutableList().remove(index)
            }
        }

        return savedFavoriteFilmsList
    }

    override fun findFilmCardWithId(filmId: String): FilmCard? {
        for (i in savedFavoriteFilmsList) {
            if (filmId == i.id) {
                val index = savedFavoriteFilmsList.indexOf(i)
                return savedFavoriteFilmsList.elementAt(index)
            }
        }

        return null
    }

    override suspend fun addSwipeFilmToSaved(film: FilmCardStandard) {
        withContext(Dispatchers.IO){
            savedSwipeFilmsDao.insertFilmToSavedSwipe(film)
        }
    }

    override suspend fun getSavedSwipeFilms(): List<FilmCardStandard> {
        return withContext(Dispatchers.IO){
            savedSwipeFilmsDao.getAllSavedSwipesFilms()
        }
    }

    suspend fun del(id: String){
        withContext(Dispatchers.IO){
            savedSwipeFilmsDao.deleteFilmFromSavedSwipe(id)
        }
    }

    override suspend fun deleteAllFromSwipe() {
        val list = getSavedSwipeFilms()
        for (i in list){
            del(i.id)
        }
    }

}