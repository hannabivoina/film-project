package com.example.myapplication.model.dataModels

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "savedFavoriteFilms")
data class FilmCard(
    @PrimaryKey var id: String = "id",
    var title: String = "title",
    var poster: String = "poster",
    @Ignore var year: String = "year",
    @Ignore var imdbRating: String = "0"
){
    fun toFilmCardStandard() = FilmCardStandard(
        id,
        title,
        poster
    )
}
