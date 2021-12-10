package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.model.dataModels.HistoryFilm

@Dao
interface HistoryFilmsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistoryToSaved(historyFilm: HistoryFilm)

    @Query("SELECT * FROM historyFilms")
    fun getAllSavedHistory():List<HistoryFilm>

    @Query("DELETE FROM historyFilms WHERE id = :filmId")
    fun deleteHistoryFromSaved(filmId: String)

    @Query("SELECT * FROM historyFilms WHERE title LIKE :filmTitle")
    fun searchHistoryInDatabase(filmTitle: String): List<HistoryFilm>
}