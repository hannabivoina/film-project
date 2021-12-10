package com.example.myapplication.apiResults.topFilms

data class TopFilmsResult(
    val errorMessage: String,
    val items: List<Item>
)