package com.example.myapplication.common.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.common.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

private const val APP_DATABASE = "app_film_database"
private const val IMDB_BASE_URL = "https://imdb-api.com"
private const val ERROR_URL = "https://httpstat.us"
private const val FIREBASE_CLOUD_FUNC_URL = "https://us-central1-course-film-project.cloudfunctions.net"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun appDatabaseProvider(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, APP_DATABASE).build()

    @ImdbRetrofitProvider
    @Singleton
    @Provides
    fun imdbRetrofitProvider(): Retrofit = Retrofit.Builder()
        .baseUrl(IMDB_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    @FirebaseRetrofitProvider
    @Singleton
    @Provides
    fun firebaseRetrofitProvider(): Retrofit = Retrofit.Builder()
        .baseUrl(FIREBASE_CLOUD_FUNC_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ImdbRetrofitProvider

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FirebaseRetrofitProvider