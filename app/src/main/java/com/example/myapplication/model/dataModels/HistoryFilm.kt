package com.example.myapplication.model.dataModels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historyFilms")
data class HistoryFilm(
    @PrimaryKey var id: String,
    var title: String
)
