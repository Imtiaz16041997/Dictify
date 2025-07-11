package com.imtiaz.dictify.di.qualifiers


import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DictionaryApi

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TranslatorApi