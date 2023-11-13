package com.meliapp.search.di

import com.meliapp.search.presentation.ProductEventRouter
import com.meliapp.search.presentation.ProductViewModel
import org.koin.dsl.bind
import org.koin.dsl.module

fun getPresentationModule() = module {
    single {
        ProductViewModel(
            getProductListUseCase = get(),
            getProductDetailsUseCase = get(),
        )
    } bind ProductEventRouter::class
}
