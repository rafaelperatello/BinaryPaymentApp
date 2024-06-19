package com.penguinpay.di

import com.penguinpay.domain.GetExchangeRateUseCase
import com.penguinpay.domain.GetExchangeRateUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Singleton
    @Binds
    abstract fun bindGetExchangeRateUseCase(source: GetExchangeRateUseCaseImpl): GetExchangeRateUseCase

}