package com.penguinpay.di

import com.penguinpay.domain.CountryProvider
import com.penguinpay.domain.CountryProviderImpl
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
    abstract fun bindCountryProvider(source: CountryProviderImpl): CountryProvider

}