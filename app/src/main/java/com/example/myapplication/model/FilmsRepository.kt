package com.example.myapplication.model

import com.example.myapplication.apiResults.companyResult.CompanyResult
import com.example.myapplication.apiResults.imdbLisrResult.ImdbListResult
import com.example.myapplication.apiResults.keywordResult.KeywordResult
import com.example.myapplication.apiResults.mostPopularResult.MostPopularResult
import com.example.myapplication.model.dataModels.FilmCard
import com.example.myapplication.model.dataModels.FilmInfo
import com.example.myapplication.model.dataModels.FilmListCategory
import com.example.myapplication.model.dataModels.ActorInfo
import retrofit2.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

const val top_const = "top"
const val soon_const = "soon"
const val dramas = ""

interface FilmsRepositoryImpl {
    suspend fun getFilmCategoriesList(catList: List<String>): List<FilmListCategory>
    suspend fun findActorsFilms(query: String): Result<ActorInfo>
    suspend fun findFilmInfo(query: String): Result<FilmInfo>
    suspend fun findFilmTrailer(query: String): Result<String?>?
    suspend fun findCompanyFilms(title: String): Response<CompanyResult>
    suspend fun findKeywordFilms(): Response<KeywordResult>
    suspend fun findImdbListFilms(query: String): Response<ImdbListResult>
    suspend fun findMostPopularFilms(): Response<MostPopularResult>
}

class FilmsRepository @Inject constructor(private val imdbApi: IMDBApi) : FilmsRepositoryImpl {

    private val categoriesList = ArrayList<FilmListCategory>()

    private suspend fun findTopFilms(): Result<List<FilmCard>> {
        return withContext(Dispatchers.IO) {
            runCatching {
                imdbApi.findFilmsTopAsync()
                    .await()
                    .takeIf {
                        it.isSuccessful
                    }
                    ?.body()
                    ?.items?.map {
                        it.toFilmCard()
                    } ?: throw Exception("Empty data")
            }
        }
    }

    private suspend fun findComingSoonFilms(): Result<List<FilmCard>> {
        return withContext(Dispatchers.IO) {
            runCatching {
                imdbApi.findFilmsComingSoonAsync()
                    .await()
                    .takeIf {
                        it.isSuccessful
                    }
                    ?.body()
                    ?.items?.map {
                        it.toFilmCard()
                    } ?: throw Exception("Empty data")
            }
        }
    }

    override suspend fun findActorsFilms(query: String): Result<ActorInfo> {
        return withContext(Dispatchers.IO) {
            runCatching {
                imdbApi.findActorFilmsAsync(query = query)
                    .await()
                    ?.takeIf { it.isSuccessful }
                    ?.body()?.toActorInfo()
                    ?: throw Exception("Empty Data")
            }
        }
    }

    override suspend fun findFilmInfo(query: String): Result<FilmInfo> {
        return withContext(Dispatchers.IO) {
            runCatching {
                imdbApi.findFilmInfoAsync(query = query)
                    .await()
                    ?.takeIf { it.isSuccessful }
                    ?.body()?.toFilmInfo()
                    ?: throw Exception("Empty Data")
            }
        }
    }

    override suspend fun getFilmCategoriesList(catList: List<String>): List<FilmListCategory> {
        categoriesList.clear()
        for (i in catList) {
            when (i) {
                "Top Films" -> searchFilms(top_const)
                "Coming Soon" -> searchFilms(soon_const)
            }
        }
        return categoriesList
    }

    private suspend fun searchFilms(query: String) {
        when (query) {
            top_const -> {
                val top = findTopFilms()
                if (top.isSuccess) {
                    top.getOrNull()?.let {
                        categoriesList.add(FilmListCategory(top_const, "Top Films", it))
                    }
                }
            }
            soon_const -> {
                val soon = findComingSoonFilms()
                if (soon.isSuccess) {
                    soon.getOrNull()?.let {
                        categoriesList.add(FilmListCategory(soon_const, "Coming Soon", it))
                    }
                }
            }
        }
    }

    private suspend fun findFilmTrailerYouTubeId(query: String): Result<String> {
        return withContext(Dispatchers.IO) {
            runCatching {
                imdbApi.findFilmTrailerIdAsync(query = query)
                    .await()
                    ?.takeIf { it.isSuccessful}
                    ?.body()?.videoId
                    ?: throw Exception("Empty Data")
            }
        }
    }

    override suspend fun findFilmTrailer(query: String): Result<String?>? {
        val youTubeId = findFilmTrailerYouTubeId(query)
        if (youTubeId.isSuccess) {
            youTubeId.getOrNull()?.let {
                return withContext(Dispatchers.IO) {
                    runCatching {
                        imdbApi.findFilmTrailerUrlAsync(query = it)
                            .await()
                            ?.takeIf { it.isSuccessful }
                            ?.body()?.getTrailerUrl()
                            ?: throw Exception("Empty Data")
                    }
                }
            }?: return null
        } else {
            return null
        }
    }

    override suspend fun findCompanyFilms(title: String): Response<CompanyResult> {
        return withContext(Dispatchers.IO) {
            imdbApi.findCompanyFilmsAsync(query = title).await()
        }
    }

    override suspend fun findKeywordFilms(): Response<KeywordResult> {
        return withContext(Dispatchers.IO) {
            imdbApi.findFilmKeywordAsync(query = dramas).await()
        }
    }

    override suspend fun findImdbListFilms(query: String): Response<ImdbListResult> {
        return withContext(Dispatchers.IO) {
            imdbApi.findImdbListAsync(query = query).await()
        }
    }

    override suspend fun findMostPopularFilms(): Response<MostPopularResult> {
        return withContext(Dispatchers.IO) {
            imdbApi.findMostPopularAsync().await()
        }
    }
}