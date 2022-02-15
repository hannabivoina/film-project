package com.example.myapplication.model.dataModels

import com.example.myapplication.apiResults.actorFilms.KnownFor

data class ActorInfo(
    val id: String,
    val name: String,
    val birthDate: String,
    val image: String,
    val role: String,
    val summary: String,
    val knownFor: List<KnownFor>,
    val awards: String
)
