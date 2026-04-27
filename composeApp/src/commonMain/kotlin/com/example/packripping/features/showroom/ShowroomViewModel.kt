package com.example.packripping.features.showroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packripping.domain.usecases.ObserveVaultUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ShowroomViewModel(
    observeVault: ObserveVaultUseCase,
) : ViewModel() {

    val uiState = observeVault()
        .map { ShowroomUiState(pulls = it, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ShowroomUiState(),
        )
}
