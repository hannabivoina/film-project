package com.example.myapplication.apiResults.comingSoonFilms

import com.example.myapplication.model.dataModels.FilmCard
import kotlin.random.Random

data class Item(
    val contentRating: String,
    val directorList: List<Director>,
    val directors: String,
    val fullTitle: String,
    val genreList: List<Genre>,
    val genres: String,
    val id: String,
    val imDbRating: String,
    val imDbRatingCount: String,
    val image: String,
    val metacriticRating: String,
    val plot: String,
    val releaseState: String,
    val runtimeMins: String,
    val runtimeStr: String,
    val starList: List<Star>,
    val stars: String,
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