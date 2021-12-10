package com.example.myapplication.apiResults.companyResult

import com.example.myapplication.model.dataModels.FilmCardStandard

data class Item(
    val id: String,
    val imDbRating: String,
    val image: String,
    val title: String,
    val year: String
){
    fun toFilmCardStandard() = FilmCardStandard(
        id,
        title,
        image
    )
}