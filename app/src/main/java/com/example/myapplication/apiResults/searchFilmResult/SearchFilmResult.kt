package com.example.myapplication.apiResults.searchFilmResult

data class SearchFilmResult(
    val errorMessage: String,
    val expression: String,
    val results: List<ResultList>,
    val searchType: String
)