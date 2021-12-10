package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.model.dataModels.FilmListCategory

@Dao
interface SavedCategoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(savedCategoriesList: List<FilmListCategory>)

    @Query("SELECT * FROM savedCategories")
    fun getAllSavedCategories():List<FilmListCategory>

    @Query("DELETE FROM savedCategories")
    fun clearAll()
}