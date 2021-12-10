package com.example.myapplication.model

import com.example.myapplication.apiResults.randomFilmsResult.Data
import com.example.myapplication.apiResults.randomFilmsResult.RandomFilmResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.ArrayList
import javax.inject.Inject

interface FirebaseRepositoryImpl {
    suspend fun findRandomFilmList(): Response<RandomFilmResult>
    fun toSaveRandomFilmsArrayList(randomDataList: List<Data>)
    fun getSaveRandomFilmsArrayList(): List<Data>
}

class FirebaseRepository @Inject constructor(private val firebaseApi: FirebaseApi) :
    FirebaseRepositoryImpl {

    val randomFilmsArrayList =  ArrayList<Data>()

    override suspend fun findRandomFilmList(): Response<RandomFilmResult> {
        return withContext(Dispatchers.IO) {
                firebaseApi.findRandomFilmsAsync().await()
            }
    }

    override fun toSaveRandomFilmsArrayList(randomDataList: List<Data>) {
        randomFilmsArrayList.clear()
        randomFilmsArrayList.addAll(randomFilmsArrayList)
    }

    override fun getSaveRandomFilmsArrayList(): List<Data> = randomFilmsArrayList

}