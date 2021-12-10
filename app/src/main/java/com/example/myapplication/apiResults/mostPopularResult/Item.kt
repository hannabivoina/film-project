package com.example.myapplication.apiResults.mostPopularResult

import com.example.myapplication.model.dataModels.FilmCardStandard

data class Item(
    val crew: String,
    val fullTitle: String,
    val id: String,
    val imDbRating: String,
    val imDbRatingCount: String,
    val image: String,
    val rank: String,
    val rankUpDown: String,
    val title: String,
    val year: String
){
    fun toFilmCardStandard() = FilmCardStandard(
        id,
        title,
        image
    )
}