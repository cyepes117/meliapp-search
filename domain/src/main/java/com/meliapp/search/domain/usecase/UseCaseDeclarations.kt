package com.meliapp.search.domain.usecase

interface UseCase<I, O> {
    suspend operator fun invoke(input: I): O
}

interface NoInputUseCase<O> {
    suspend operator fun invoke(): O
}