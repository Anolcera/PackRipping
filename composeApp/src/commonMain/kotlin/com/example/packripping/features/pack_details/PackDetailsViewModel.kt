package com.example.packripping.features.pack_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packripping.core.network.ApiResult
import com.example.packripping.core.network.ErrorType
import com.example.packripping.domain.usecases.GetPackDetailsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PackDetailsViewModel(
    private val packId: Int,
    private val getPackDetails: GetPackDetailsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PackDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigation = Channel<PackDetailsNavigationEvent>(Channel.BUFFERED)
    val navigation = _navigation.receiveAsFlow()

    private var loadJob: Job? = null

    init {
        load()
    }

    fun onEvent(event: PackDetailsEvent) {
        when (event) {
            PackDetailsEvent.Refresh -> load()
            PackDetailsEvent.BuyClicked -> requestReveal()
            PackDetailsEvent.Dismissed -> viewModelScope.launch {
                _navigation.send(PackDetailsNavigationEvent.Dismiss)
            }
            PackDetailsEvent.DismissError -> _uiState.update { it.copy(errorMessage = null) }
        }
    }

    private fun load() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getPackDetails(packId).collect { result ->
                _uiState.update { current ->
                    when (result) {
                        is ApiResult.Success -> current.copy(
                            isLoading = false,
                            pack = result.data,
                            errorMessage = null,
                        )
                        is ApiResult.SuccessNobody -> current.copy(
                            isLoading = false,
                            errorMessage = "Pack not found.",
                        )
                        is ApiResult.Error -> current.copy(
                            isLoading = false,
                            errorMessage = result.type.toMessage(),
                        )
                    }
                }
            }
        }
    }

    private fun requestReveal() {
        val pack = _uiState.value.pack ?: return
        viewModelScope.launch {
            _navigation.send(PackDetailsNavigationEvent.RequestPullReveal(pack))
        }
    }
}

private fun ErrorType.toMessage(): String = when (this) {
    is ErrorType.Network -> "No connection — check your network and try again."
    is ErrorType.Http -> "Server error (${code ?: "?"}). Please try again."
    is ErrorType.Custom<*> -> "Unexpected response."
    is ErrorType.Unknown -> "Something went wrong."
}
