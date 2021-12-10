package com.example.myapplication.model

import com.example.myapplication.apiResults.randomFilmsResult.RandomFilmResult
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
//https://us-central1-course-film-project.cloudfunctions.net/getRandomFilms
interface FirebaseApi {
    @GET("getRandomFilms")
    fun findRandomFilmsAsync(): Deferred<Response<RandomFilmResult>>
}