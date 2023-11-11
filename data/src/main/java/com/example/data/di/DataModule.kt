package com.example.data.di

import com.example.data.ProductMapper
import com.example.data.ProductMapperImpl
import com.example.data.ProductRepositoryImpl
import com.example.data.api.ProductApiService
import com.example.data.api.RemoteProductDataSource
import com.example.data.api.RemoteProductDataSourceImpl
import com.example.data.local.AppDatabase
import com.example.data.local.LocalProductDataSource
import com.example.data.local.LocalProductDataSourceImpl
import com.example.data.local.ProductDao
import com.example.domain.repository.ProductRepository
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
