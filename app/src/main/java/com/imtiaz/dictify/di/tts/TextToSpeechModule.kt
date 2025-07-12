package com.imtiaz.dictify.di.tts

import com.imtiaz.dictify.data.service.TextToSpeechServiceImpl
import com.imtiaz.dictify.domain.service.TextToSpeechService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TextToSpeechModule {
    @Binds
    @Singleton
    abstract fun bindTextToSpeechService(impl: TextToSpeechServiceImpl): TextToSpeechService
}