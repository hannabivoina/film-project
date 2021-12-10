package com.example.myapplication.apiResults.comingSoonFilms

data class ComingSoonResult(
    val errorMessage: String,
    val items: List<Item>
)