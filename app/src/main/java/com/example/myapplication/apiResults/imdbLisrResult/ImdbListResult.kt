package com.example.myapplication.apiResults.imdbLisrResult

data class ImdbListResult(
    val `by`: String,
    val created: String,
    val description: String,
    val errorMessage: String,
    val items: List<Item>,
    val title: String,
    val updated: String
)