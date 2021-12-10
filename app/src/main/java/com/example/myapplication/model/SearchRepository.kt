package com.example.myapplication.model

import com.example.myapplication.database.HistoryFilmsDao
import com.example.myapplication.model.dataModels.HistoryFilm
import com.example.myapplication.model.dataModels.HistoryFilmCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface SearchRepositoryImpl{
    suspend fun getAllSavedHistory(): List<HistoryFilm>
    suspend fun insertHistoryToSaved(historyFilm: HistoryFilm)
    suspend fun searchHistoryInDatabase(historyFilm: String): List<HistoryFilm>
    suspend fun findFilmByName(query: String): Result<List<HistoryFilm>>
    suspend fun findHistoryFilmCard(query: String): Result<HistoryFilmCard>
}

class SearchRepository @Inject constructor(
    private val historyFilmsDao: HistoryFilmsDao,
    private val imdbApi: IMDBApi
): SearchRepositoryImpl {

    override suspend fun getAllSavedHistory(): List<HistoryFilm> {
        return withContext(Dispatchers.IO) {
                historyFilmsDao.getAllSavedHistory()
            }
    }

    override suspend fun insertHistoryToSaved(historyFilm: HistoryFilm) {
        withContext(Dispatchers.IO) {
            historyFilmsDao.insertHistoryToSaved(historyFilm)
        }
    }

    override suspend fun searchHistoryInDatabase(historyFilm: String): List<HistoryFilm> {
        return withContext(Dispatchers.IO) {
            historyFilmsDao.searchHistoryInDatabase(historyFilm)
        }
    }

    override suspend fun findFilmByName(query: String): Result<List<HistoryFilm>> {
        return withContext(Dispatchers.IO) {
            runCatching {
                imdbApi.findFilmByNameAsync(query = query)
                    .await()
                    .takeIf {
                        it.isSuccessful
                    }
                    ?.body()
                    ?.results?.map {
                        it.toFilmHistory()
                    } ?: throw Exception("Empty data")
            }
        }
    }

    override suspend fun findHistoryFilmCard(query: String): Result<HistoryFilmCard> {
        return withContext(Dispatchers.IO) {
            runCatching {
                imdbApi.findFilmInfoAsync(query = query)
                    .await()
                    ?.takeIf { it.isSuccessful }
                    ?.body()?.toHistoryFilmCard()
                    ?: throw java.lang.Exception("Empty Data")
            }
        }
    }
}