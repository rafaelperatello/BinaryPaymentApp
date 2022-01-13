package com.penguinpay.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

const val CONTEXT_IO = "CONTEXT_IO"
const val CONTEXT_COMPUTATION = "CONTEXT_COMPUTATION"
const val CONTEXT_MAIN = "CONTEXT_MAIN"

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {

    @Provides
    @Named(CONTEXT_IO)
    fun provideContextIo(): CoroutineContext = Dispatchers.IO

    @Provides
    @Named(CONTEXT_COMPUTATION)
    fun provideContextComputation(): CoroutineContext = Dispatchers.Default

    @Provides
    @Named(CONTEXT_MAIN)
    fun provideContextMain(): CoroutineContext = Dispatchers.Main
}