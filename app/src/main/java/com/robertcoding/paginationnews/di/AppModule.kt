package com.robertcoding.paginationnews.di

import com.robertcoding.paginationnews.components.singlearticle.SingleArticleViewModel
import com.robertcoding.paginationnews.viewmodel.LatestNewzyViewModel
import com.robertcoding.paginationnews.viewmodel.MainViewModel
import com.robertcoding.paginationnews.viewmodel.SearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::LatestNewzyViewModel)
    viewModelOf(::SingleArticleViewModel)
}