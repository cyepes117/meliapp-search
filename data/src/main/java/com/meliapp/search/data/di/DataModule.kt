package com.meliapp.search.data.di

import com.meliapp.search.data.ProductMapper
import com.meliapp.search.data.ProductMapperImpl
import com.meliapp.search.data.ProductRepositoryImpl
import com.meliapp.search.data.api.ProductApiService
import com.meliapp.search.data.api.RemoteProductDataSource
import com.meliapp.search.data.api.RemoteProductDataSourceImpl
import com.meliapp.search.data.local.AppDatabase
import com.meliapp.search.data.local.LocalProductDataSource
import com.meliapp.search.data.local.LocalProductDataSourceImpl
import com.meliapp.search.data.local.ProductDao
import com.meliapp.search.domain.repository.ProductRepository
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

fun getDataModule() = module {
    single {
        ProductRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get(),
            productMapper = get(),
        )
    } bind ProductRepository::class

    single {
        RemoteProductDataSourceImpl(
            apiService = get(),
        )
    } bind RemoteProductDataSource::class

    single {
        LocalProductDataSourceImpl(
            productDao = get(),
        )
    } bind LocalProductDataSource::class

    single {
        AppDatabase
            .getInstance(context = get())
            .productDao()
    } bind ProductDao::class

    single {
        Retrofit.Builder()
            .baseUrl("https://api.example.com/")
            .build()
            .create(ProductApiService::class.java)
    } bind ProductApiService::class

    single {
        ProductMapperImpl()
    } bind ProductMapper::class
}
