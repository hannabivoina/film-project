package com.example.myapplication.database

import androidx.room.*
import com.example.myapplication.model.dataModels.FilmCard

@Dao
interface SavedFilmsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilmToFavorite(savedFilm: FilmCard)

    @Query("SELECT * FROM savedFavoriteFilms")
    fun getAllSavedFilms():List<FilmCard>

    @Query("DELETE FROM savedFavoriteFilms WHERE id = :filmId")
    fun deleteFilmFromFavorite(filmId: String)
}
