package com.example.myapplication.apiResults.actorFilms

import com.example.myapplication.model.dataModels.ActorInfo

data class ActorFilmsResult(
    val awards: String,
    val birthDate: String,
    val castMovies: List<Any>,
    val deathDate: Any,
    val errorMessage: String,
    val height: String,
    val id: String,
    val image: String,
    val knownFor: List<KnownFor>,
    val name: String,
    val role: String,
    val summary: String
) {
    fun toActorInfo() = ActorInfo(
        id,
        name,
        birthDate = "Born: $birthDate",
        image,
        role,
        summary,
        knownFor,
        awards
    )
}