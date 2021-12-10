package com.example.myapplication.model.dataModels.swipeModel

import com.example.myapplication.model.dataModels.FilmCardStandard

data class SwipeRightCardModel(
    val filmId: String,
    val filmImage: String,
    val filmTitle: String,
    val filmFullTitle: String,
    val filmYear: String,
    val filmGenreList: String,
    val filmPlot: String
) {
    fun toFilmCardStandard() = FilmCardStandard(
        filmId,
        filmTitle,
        filmImage
    )
}
