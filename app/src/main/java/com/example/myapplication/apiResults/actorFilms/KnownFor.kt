package com.example.myapplication.apiResults.actorFilms

import com.example.myapplication.model.dataModels.FilmCard
import com.example.myapplication.model.dataModels.FilmCardStandard
import kotlin.random.Random

data class KnownFor(
    val fullTitle: String,
    val id: String,
    val image: String,
    val role: String,
    val title: String,
    val year: String
){
    fun toFilmCard() = FilmCard(
        id,
        title,
        image,
        year,
        imdbRating = (Random.nextInt(20,100).toFloat()/10).toString()
    )

    fun toFilmCardStandard() = FilmCardStandard(
        id,
        title,
        image
    )
}