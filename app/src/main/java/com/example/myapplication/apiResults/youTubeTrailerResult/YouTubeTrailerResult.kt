package com.example.myapplication.apiResults.youTubeTrailerResult

data class YouTubeTrailerResult(
    val errorMessage: String,
    val fullTitle: String,
    val imDbId: String,
    val title: String,
    val type: String,
    val videoId: String,
    val videoUrl: String,
    val year: String
)