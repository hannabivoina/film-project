package com.example.myapplication.apiResults.imdbLisrResult

import com.example.myapplication.model.dataModels.FilmCardStandard

data class Item(
    val description: String,
    val fullTitle: String,
    val id: String,
    val imDbRating: String,
    val imDbRatingCount: String,
    val image: String,
    val index: String,
    val title: String,
    val year: String
){
    fun toFilmCardStandard() = FilmCardStandard(
        id,
        title,
        image
    )
}