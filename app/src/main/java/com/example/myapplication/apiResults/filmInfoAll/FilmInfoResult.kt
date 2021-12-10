package com.example.myapplication.apiResults.filmInfoAll

import com.example.myapplication.model.dataModels.FilmCardStandard
import com.example.myapplication.model.dataModels.FilmInfo
import com.example.myapplication.model.dataModels.HistoryFilmCard
import com.example.myapplication.model.dataModels.swipeModel.SwipeRightCardModel
import kotlin.random.Random

data class FilmInfoResult(
    val actorList: List<Actor>,
    val awards: String,
    val boxOffice: BoxOffice,
    val companies: String,
    val companyList: List<Company>,
    val contentRating: String,
    val countries: String,
    val countryList: List<Country>,
    val directorList: List<Director>,
    val directors: String,
    val errorMessage: String,
    val fullCast: Any,
    val fullTitle: String,
    val genreList: List<Genre>,
    val genres: String,
    val id: String,
    val imDbRating: String,
    val imDbRatingVotes: String,
    val image: String,
    val images: Any,
    val keywordList: List<String>,
    val keywords: String,
    val languageList: List<Language>,
    val languages: String,
    val metacriticRating: String,
    val originalTitle: String,
    val plot: String,
    val plotLocal: String,
    val plotLocalIsRtl: Boolean,
    val posters: Any,
    val ratings: Any,
    val releaseDate: String,
    val runtimeMins: String,
    val runtimeStr: String,
    val similars: List<Similar>,
    val starList: List<Star>,
    val stars: String,
    val tagline: String,
    val title: String,
    val trailer: Any,
    val tvEpisodeInfo: Any,
    val tvSeriesInfo: Any,
    val type: String,
    val wikipedia: Any,
    val writerList: List<Writer>,
    val writers: String,
    val year: String
){
    fun toFilmInfo() = FilmInfo(
        id,
        title,
        year,
        genres,
        fullTitle,
        imDbRating = if(imDbRating == ""){
            (Random.nextInt(20,100).toFloat()/10).toString()} else {imDbRating},
        image,
        plot,
        directors,
        actorList = if (actorList.size == 5) {actorList} else {actorList.subList(0, 5)},
        similars.map { it.toFilmCardStandard()},
        type,
        runtimeStr
    )

    fun toHistoryFilmCard() = HistoryFilmCard(
        id,
        title,
        image,
        year,
        imDbRating,
        genres
    )

    fun toFilmCardStandard() = FilmCardStandard(
        id,
        title,
        image
    )

    fun toFilmSwipeStandard() = SwipeRightCardModel(
        id,
        image,
        title,
        fullTitle,
        year,
        genres,
        plot
    )
}