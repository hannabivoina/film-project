package com.example.myapplication.apiResults.filmInfoAll

import com.example.myapplication.model.dataModels.FilmCard
import com.example.myapplication.model.dataModels.FilmCardStandard
import kotlin.random.Random

data class Similar(
    val directors: String,
    val fullTitle: String,
    val genres: String,
    val id: String,
    val imDbRating: String,
    val image: String,
    val plot: String,
    val stars: String,
    val title: String,
    val year: String
){
    fun toFilmCard() = FilmCard(
        id,
        title,
        image,
        year,
        imdbRating = if(imDbRating == "" || imDbRating == "-"){
            (Random.nextInt(20,100).toFloat()/10).toString()} else {imDbRating},
    )

    fun toFilmCardStandard() = FilmCardStandard(
        id,
        title,
        image
    )
}