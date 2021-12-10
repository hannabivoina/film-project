package com.example.myapplication.apiResults.keywordResult

data class KeywordResult(
    val errorMessage: String,
    val items: List<Item>,
    val keyword: String
)