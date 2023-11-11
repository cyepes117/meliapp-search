package com.example.domain.di

import com.example.domain.usecase.GetProductDetailsUseCase
import com.example.domain.usecase.GetProductDetailsUseCaseImpl
import com.example.domain.usecase.GetProductListUseCase
import com.example.domain.usecase.GetProductListUseCaseImpl
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
