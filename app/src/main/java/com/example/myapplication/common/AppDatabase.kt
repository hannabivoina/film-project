package com.example.myapplication.common

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.database.HistoryFilmsDao
import com.example.myapplication.database.SavedCategoriesDao
import com.example.myapplication.database.SavedFilmsDao
import com.example.myapplication.database.SavedSwipeFilmsDao
import com.example.myapplication.model.dataModels.*

@Database(entities = [FilmListCategory::class, FilmCard::class, HistoryFilm::class, FilmCardStandard::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getSavedCategoriesDao(): SavedCategoriesDao
    abstract fun getSavedFilmsDao(): SavedFilmsDao
    abstract fun getHistoryFilmsDao(): HistoryFilmsDao
    abstract fun getSavedSwipeDao(): SavedSwipeFilmsDao
}