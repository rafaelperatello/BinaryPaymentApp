package com.penguinpay.di

import com.penguinpay.data.exchangerate.remote.ExchangeRateApi
import com.penguinpay.data.exchangerate.local.ExchangeRateLocalDataSource
import com.penguinpay.data.exchangerate.local.ExchangeRateLocalDataSourceImpl
import com.penguinpay.data.exchangerate.remote.ExchangeRateRemoteDataSource
import com.penguinpay.data.exchangerate.remote.ExchangeRateRemoteDataSourceImpl
import com.penguinpay.data.exchangerate.ExchangeRateRepositoryImpl
import com.penguinpay.domain.repository.ExchangeRateRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindExchangeRateLocalDataSource(source: ExchangeRateLocalDataSourceImpl): ExchangeRateLocalDataSource

    @Singleton
    @Binds
    abstract fun bindExchangeRateRemoteDataSource(source: ExchangeRateRemoteDataSourceImpl): ExchangeRateRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindExchangeRateRepository(source: ExchangeRateRepositoryImpl): ExchangeRateRepository

    companion object {

        @Singleton
        @Provides
        fun provideExchangeRateApi(retrofit: Retrofit): ExchangeRateApi =
            retrofit.create(ExchangeRateApi::class.java)
    }
}