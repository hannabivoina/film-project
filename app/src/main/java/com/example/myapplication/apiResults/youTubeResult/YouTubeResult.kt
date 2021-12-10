package com.example.myapplication.apiResults.youTubeResult

data class YouTubeResult(
    val description: String,
    val duration: String,
    val errorMessage: String,
    val image: String,
    val title: String,
    val uploadDate: String,
    val videoId: String,
    val videos: List<Video>
){
    fun getTrailerUrl(): String?{
        return if (videos.isNotEmpty()){
            videos[0].url
        } else {
            null
        }
    }
}