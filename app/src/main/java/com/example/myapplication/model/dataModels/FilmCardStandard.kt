package com.example.myapplication.model.dataModels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savedSwipeFilms")
data class FilmCardStandard(
    @PrimaryKey var id: String,
    var title: String,
    var poster: String
    )