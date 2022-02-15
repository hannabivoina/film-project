package com.example.myapplication.model

import com.example.myapplication.apiResults.actorFilms.ActorFilmsResult
import com.example.myapplication.apiResults.comingSoonFilms.ComingSoonResult
import com.example.myapplication.apiResults.companyResult.CompanyResult
import com.example.myapplication.apiResults.filmInfoAll.FilmInfoResult
import com.example.myapplication.apiResults.imdbLisrResult.ImdbListResult
import com.example.myapplication.apiResults.keywordResult.KeywordResult
import com.example.myapplication.apiResults.mostPopularResult.MostPopularResult
import com.example.myapplication.apiResults.searchFilmResult.SearchFilmResult
import com.example.myapplication.apiResults.topFilms.TopFilmsResult
import com.example.myapplication.apiResults.youTubeResult.YouTubeResult
import com.example.myapplication.apiResults.youTubeTrailerResult.YouTubeTrailerResult
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

private const val KEY_CONST = "KEY_CONST"
private const val QUERY_CONST = "query"
//private const val API_KEY_IMDB = "k_g6p8l8m7"
private const val API_KEY_IMDB = "k_cf30v1j7"
//private const val API_KEY_IMDB = "k_iiogeu1r"

interface IMDBApi {

    @GET("en/API/Top250Movies/{$KEY_CONST}")
    fun findFilmsTopAsync(
        @Path(KEY_CONST) key: String = API_KEY_IMDB
    ): Deferred<Response<TopFilmsResult>>

    @GET("en/API/Name/{$KEY_CONST}/{$QUERY_CONST}")
    fun findActorFilmsAsync(
        @Path(KEY_CONST) key: String = API_KEY_IMDB,
        @Path(QUERY_CONST) query: String
    ): Deferred<Response<ActorFilmsResult>>

    @GET("en/API/ComingSoon/{$KEY_CONST}")
    fun findFilmsComingSoonAsync(
        @Path(KEY_CONST) key: String = API_KEY_IMDB
    ): Deferred<Response<ComingSoonResult>>

    @GET("en/API/Title/{$KEY_CONST}/{$QUERY_CONST}")
    fun findFilmInfoAsync(
        @Path(KEY_CONST) key: String = API_KEY_IMDB,
        @Path(QUERY_CONST) query: String
    ):Deferred<Response<FilmInfoResult>>

    @GET("en/API/SearchMovie/{$KEY_CONST}/{$QUERY_CONST}")
    fun findFilmByNameAsync(
        @Path(KEY_CONST) key: String = API_KEY_IMDB,
        @Path(QUERY_CONST) query: String
    ):Deferred<Response<SearchFilmResult>>

    @GET("en/API/Keyword/{$KEY_CONST}/{$QUERY_CONST}")
    fun findFilmKeywordAsync(
        @Path(KEY_CONST) key: String = API_KEY_IMDB,
        @Path(QUERY_CONST) query: String
    ):Deferred<Response<KeywordResult>>

    @GET("API/YouTubeTrailer/{$KEY_CONST}/{$QUERY_CONST}")
    fun findFilmTrailerIdAsync(
        @Path(KEY_CONST) key: String = API_KEY_IMDB,
        @Path(QUERY_CONST) query: String
    ):Deferred<Response<YouTubeTrailerResult>>

    @GET("en/API/YouTube/{$KEY_CONST}/{$QUERY_CONST}")
    fun findFilmTrailerUrlAsync(
        @Path(KEY_CONST) key: String = API_KEY_IMDB,
        @Path(QUERY_CONST) query: String
    ):Deferred<Response<YouTubeResult>>

    @GET("en/API/Company/{$KEY_CONST}/{$QUERY_CONST}")
    fun findCompanyFilmsAsync(
        @Path(KEY_CONST) key: String = API_KEY_IMDB,
        @Path(QUERY_CONST) query: String
    ):Deferred<Response<CompanyResult>>

    @GET("en/API/IMDbList/{$KEY_CONST}/{$QUERY_CONST}")
    fun findImdbListAsync(
        @Path(KEY_CONST) key: String = API_KEY_IMDB,
        @Path(QUERY_CONST) query: String
    ):Deferred<Response<ImdbListResult>>

    @GET("en/API/MostPopularMovies/{$KEY_CONST}")
    fun findMostPopularAsync(
        @Path(KEY_CONST) key: String = API_KEY_IMDB
    ): Deferred<Response<MostPopularResult>>

}