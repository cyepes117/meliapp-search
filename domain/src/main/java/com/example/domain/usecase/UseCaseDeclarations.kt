package com.example.domain.usecase

interface UseCase<I, O> {
    suspend fun invoke(input: I): O
}

interface NoInputUseCase<O> {
    suspend fun invoke(): O
}