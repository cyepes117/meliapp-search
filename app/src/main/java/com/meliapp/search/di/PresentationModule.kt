package com.meliapp.search.di

import com.meliapp.search.ProductEventRouter
import com.meliapp.search.ProductViewModel
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
