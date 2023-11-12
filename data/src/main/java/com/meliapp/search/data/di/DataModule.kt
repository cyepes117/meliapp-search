package com.meliapp.search.data.di

import com.meliapp.search.data.ProductMapper
import com.meliapp.search.data.ProductMapperImpl
import com.meliapp.search.data.ProductRepositoryImpl
import com.meliapp.search.data.api.EitherCallAdapterFactory
import com.meliapp.search.data.api.ProductApiService
import com.meliapp.search.data.api.RemoteProductDataSource
import com.meliapp.search.data.api.RemoteProductDataSourceImpl
import com.meliapp.search.data.local.AppDatabase
import com.meliapp.search.data.local.LocalProductDataSource
import com.meliapp.search.data.local.LocalProductDataSourceImpl
import com.meliapp.search.data.local.ProductDao
import com.meliapp.search.domain.repository.ProductRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } bind OkHttpClient::class

    single {
        Retrofit.Builder()
            .baseUrl("https://api.mercadolibre.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(EitherCallAdapterFactory())
            .build()
            .create(ProductApiService::class.java)
    } bind ProductApiService::class

    single {
        ProductMapperImpl()
    } bind ProductMapper::class
}
