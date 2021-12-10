package com.example.myapplication.common.di

import com.example.myapplication.common.AppDatabase
import com.example.myapplication.database.HistoryFilmsDao
import com.example.myapplication.database.SavedCategoriesDao
import com.example.myapplication.database.SavedFilmsDao
import com.example.myapplication.database.SavedSwipeFilmsDao
import com.example.myapplication.model.FirebaseApi
import com.example.myapplication.model.IMDBApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
object MainModule {

    @ActivityRetainedScoped
    @Provides
    fun imdbApiProvider(@ImdbRetrofitProvider retrofit: Retrofit): IMDBApi = retrofit.create(IMDBApi::class.java)

    @ActivityRetainedScoped
    @Provides
    fun firebaseApiProvider(@FirebaseRetrofitProvider retrofit: Retrofit): FirebaseApi = retrofit.create(FirebaseApi::class.java)

    @ActivityRetainedScoped
    @Provides
    fun savedCategoriesDaoProvider(appDatabase: AppDatabase): SavedCategoriesDao = appDatabase.getSavedCategoriesDao()

    @ActivityRetainedScoped
    @Provides
    fun savedFilmsDaoProvider(appDatabase: AppDatabase): SavedFilmsDao = appDatabase.getSavedFilmsDao()

    @ActivityRetainedScoped
    @Provides
    fun historyFilmsDaoProvider(appDatabase: AppDatabase): HistoryFilmsDao = appDatabase.getHistoryFilmsDao()

    @ActivityRetainedScoped
    @Provides
    fun savedSwipeFilmsDaoProvider(appDatabase: AppDatabase): SavedSwipeFilmsDao = appDatabase.getSavedSwipeDao()
}