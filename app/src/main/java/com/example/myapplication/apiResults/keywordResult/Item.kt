package com.example.myapplication.apiResults.keywordResult

import com.example.myapplication.model.dataModels.FilmCard
import com.example.myapplication.model.dataModels.FilmCardStandard
import kotlin.random.Random

data class Item(
    val id: String,
    val imDbRating: String,
    val image: String,
    val title: String,
    val year: String
){
    fun toFilmCard() = FilmCard(
        id,
        title,
        image,
        year,
        imdbRating = if(imDbRating == "-" || imDbRating == ""){
            (Random.nextInt(20,100).toFloat()/10).toString()} else {imDbRating}
    )

    fun toFilmCardStandard() = FilmCardStandard(
        id,
        title,
        image
    )
}