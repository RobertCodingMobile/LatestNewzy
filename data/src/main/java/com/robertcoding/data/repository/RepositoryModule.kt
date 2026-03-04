package com.robertcoding.data.repository

import com.robertcoding.domain.repository.ArticleRepository
import com.robertcoding.domain.repository.LatestNewsRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<ArticleRepository> { ArticleRepositoryImpl(get()) }
    single<LatestNewsRepository> { LatestNewsRepositoryImpl(get()) }
}