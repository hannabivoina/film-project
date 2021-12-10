package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.model.dataModels.FilmCardStandard

@Dao
interface SavedSwipeFilmsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilmToSavedSwipe(savedSwipe: FilmCardStandard)

    @Query("SELECT * FROM savedSwipeFilms")
    fun getAllSavedSwipesFilms():List<FilmCardStandard>

    @Query("DELETE FROM savedSwipeFilms WHERE id = :filmId")
    fun deleteFilmFromSavedSwipe(filmId: String)

    @Query("DELETE FROM savedSwipeFilms WHERE id = :filmId")
    fun deleteAllFilmFromSavedSwipe(filmId: String)
}