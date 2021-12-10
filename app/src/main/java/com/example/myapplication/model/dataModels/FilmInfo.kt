package com.example.myapplication.model.dataModels

import com.example.myapplication.apiResults.filmInfoAll.Actor
import com.example.myapplication.model.dataModels.swipeModel.SwipeRightCardModel

data class FilmInfo(
    val id: String,
    val title: String,
    val year: String,
    val genres: String,
    val fullTitle: String,
    val imDbRating: String,
    val poster: String,
    val plot: String,
    val director: String,
    val actorList: List<Actor>,
    val similars: List<FilmCardStandard>,
    val type: String,
    val runtimeStr: String
) {
    fun toFilmCard() = FilmCard(
        id,
        title,
        poster,
        year,
        imDbRating
    )

    fun toFilmSwipeStandard() = SwipeRightCardModel(
        id,
        poster,
        title,
        fullTitle,
        year,
        genres,
        plot
    )
}
