package com.imtiaz.dictify.di.network


import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DictionaryApi

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TranslatorApi

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RandomWordApi