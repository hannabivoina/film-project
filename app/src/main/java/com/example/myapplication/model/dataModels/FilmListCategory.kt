package com.example.myapplication.model.dataModels

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.myapplication.model.dataModels.FilmCard
import java.util.Collections.emptyList

@Entity(tableName = "savedCategories")
data class FilmListCategory (
    @Ignore var id: String = "id",
    @PrimaryKey var title: String = "title",
    @Ignore var filmList: List<FilmCard> = emptyList()
)