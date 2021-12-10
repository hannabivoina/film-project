package com.example.myapplication.apiResults.searchFilmResult

import com.example.myapplication.model.dataModels.FilmCard
import com.example.myapplication.model.dataModels.HistoryFilm

data class ResultList(
    val description: String,
    val id: String,
    val image: String,
    val resultType: String,
    val title: String
){
    fun toFilmHistory() = HistoryFilm(
        id = id,
        title = "$title, ${description.substring(1, 5)}"
    )
}