package com.meliapp.search.domain.di

import com.meliapp.search.domain.usecase.GetProductDetailsUseCase
import com.meliapp.search.domain.usecase.GetProductDetailsUseCaseImpl
import com.meliapp.search.domain.usecase.GetProductListUseCase
import com.meliapp.search.domain.usecase.GetProductListUseCaseImpl
import org.koin.dsl.bind
import org.koin.dsl.module

fun getDomainModule() = module {
    single {
        GetProductListUseCaseImpl(productRepository = get())
    } bind GetProductListUseCase::class

    single {
        GetProductDetailsUseCaseImpl(productRepository = get())
    } bind GetProductDetailsUseCase::class
}
