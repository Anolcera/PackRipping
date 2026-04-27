package com.example.packripping.features.pulls

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packripping.domain.usecases.ObservePullsUseCase
import com.example.packripping.domain.usecases.ObserveWalletUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class PullsViewModel(
    observePulls: ObservePullsUseCase,
    observeWallet: ObserveWalletUseCase,
) : ViewModel() {

    val uiState = combine(observePulls(), observeWallet()) { pulls, wallet ->
        PullsUiState(pulls = pulls, walletCents = wallet, isLoading = false)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PullsUiState(),
    )
}
