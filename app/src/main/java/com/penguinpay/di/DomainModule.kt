package com.penguinpay.di

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.penguinpay.domain.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Singleton
    @Binds
    abstract fun bindCountryProvider(source: CountryProviderImpl): CountryProvider

    @Singleton
    @Binds
    abstract fun bindExchangeRateInteractor(source: ExchangeRateInteractorImpl): ExchangeRateInteractor

    @Singleton
    @Binds
    abstract fun bindPhoneNumberInteractor(source: PhoneNumberInteractorImpl): PhoneNumberInteractor

    companion object {

        @Singleton
        @Provides
        fun providePhoneNumberUtil(): PhoneNumberUtil {
            return PhoneNumberUtil.getInstance()
        }
    }
}