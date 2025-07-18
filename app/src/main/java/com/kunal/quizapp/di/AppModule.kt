package com.kunal.quizapp.di

import com.kunal.quizapp.data.QuizRepositoryImpl
import com.kunal.quizapp.domain.QuizRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun providesRepository(
        quizRepositoryImpl: QuizRepositoryImpl
    ) : QuizRepository
}