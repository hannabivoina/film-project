package com.example.myapplication.apiResults.companyResult

data class CompanyResult(
    val errorMessage: String,
    val id: String,
    val items: List<Item>,
    val name: String
)