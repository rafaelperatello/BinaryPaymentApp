package com.penguinpay.di

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.penguinpay.util.CountryProvider
import com.penguinpay.util.CountryProviderImpl
import com.penguinpay.util.PhoneNumberHelper
import com.penguinpay.util.PhoneNumberHelperImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UtilModule {

    @Singleton
    @Binds
    abstract fun bindCountryProvider(source: CountryProviderImpl): CountryProvider


    @Singleton
    @Binds
    abstract fun bindPhoneNumberHelper(source: PhoneNumberHelperImpl): PhoneNumberHelper

    companion object {

        @Singleton
        @Provides
        fun providePhoneNumberUtil(): PhoneNumberUtil {
            return PhoneNumberUtil.getInstance()
        }
    }
}