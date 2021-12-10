package com.example.myapplication.common.di

import androidx.lifecycle.ViewModel
import com.example.myapplication.SwipeRightViewModel
import com.example.myapplication.model.*
import com.example.myapplication.viewModel.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class MainBindModule {

    @ActivityRetainedScoped
    @Binds
    abstract fun bindFilmRepository(filmsRepository: FilmsRepository): FilmsRepositoryImpl

    @ActivityRetainedScoped
    @Binds
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @ActivityRetainedScoped
    @Binds
    abstract fun bindProfileRepository(profileRepository: ProfileRepository): ProfileRepositoryImpl

    @ActivityRetainedScoped
    @Binds
    abstract fun bindProfileViewModel(profileViewModel: ProfileViewModel): ViewModel

    @ActivityRetainedScoped
    @Binds
    abstract fun bindFilmViewModel(filmViewModel: FilmViewModel): ViewModel

    @ActivityRetainedScoped
    @Binds
    abstract fun bindActorsFilmViewModel(actorFilmsViewModel: ActorFilmsViewModel): ViewModel

    @ActivityRetainedScoped
    @Binds
    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @ActivityRetainedScoped
    @Binds
    abstract fun bindSearchRepository(searchRepository: SearchRepository): SearchRepositoryImpl

    @ActivityRetainedScoped
    @Binds
    abstract fun bindSearchResultViewModel(searchResultViewModel: SearchResultViewModel): ViewModel

    @ActivityRetainedScoped
    @Binds
    abstract fun bindFirebaseRepository(firebaseRepository: FirebaseRepository): FirebaseRepositoryImpl

    @ActivityRetainedScoped
    @Binds
    abstract fun bindSwipeViewModel(matchesViewModel: SwipeViewModel): ViewModel

    @ActivityRetainedScoped
    @Binds
    abstract fun bindSwipeRightViewModel(swipeRightViewModel: SwipeRightViewModel): ViewModel

    @ActivityRetainedScoped
    @Binds
    abstract fun bindCollectionViewModel(collectionViewModel: CollectionViewModel): ViewModel
}