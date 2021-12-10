package com.example.myapplication.apiResults.topFilms

import com.example.myapplication.model.dataModels.FilmCard
import kotlin.random.Random

data class Item(
    val crew: String,
    val fullTitle: String,
    val id: String,
    val imDbRating: String,
    val imDbRatingCount: String,
    val image: String,
    val rank: String,
    val title: String,
    val year: String
){
    fun toFilmCard() = FilmCard(
        id,
        title,
        image,
        year,
        imdbRating = if(imDbRating == ""){
            (Random.nextInt(20,100).toFloat()/10).toString()} else {imDbRating}

    )
}