package com.robertcoding.data.di

import com.robertcoding.data.repository.ArticleRepositoryImpl
import com.robertcoding.data.repository.LatestNewsRepositoryImpl
import com.robertcoding.data.settings.repository.UserPreferencesRepositoryImpl
import com.robertcoding.domain.repository.ArticleRepository
import com.robertcoding.domain.repository.LatestNewsRepository
import com.robertcoding.domain.repository.UserPreferencesRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<ArticleRepository> { ArticleRepositoryImpl(get()) }
    single<LatestNewsRepository> { LatestNewsRepositoryImpl(get()) }
    single<UserPreferencesRepository> { UserPreferencesRepositoryImpl(get()) }
}