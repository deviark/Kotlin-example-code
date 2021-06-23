package com.lotteryadviser.presentation.base

class BaseUiState<T>(
    val data: T,
    val state: State
)

sealed class State{
    object Loading: State()
    object Loaded: State()
    object Error: State()
}