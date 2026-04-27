package com.example.packripping.features.packs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packripping.core.common.ErrorParser
import com.example.packripping.core.network.ApiResult
import com.example.packripping.domain.usecases.GetPacksUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PacksViewModel(
    private val getPacks: GetPacksUseCase,
    private val errorParser: ErrorParser,
) : ViewModel() {

    private val _uiState = MutableStateFlow<PacksUiState>(PacksUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _navigation = Channel<PacksNavigationEvent>(Channel.BUFFERED)
    val navigation = _navigation.receiveAsFlow()

    private var loadJob: Job? = null

    init {
        load()
    }

    fun onEvent(event: PacksEvent) {
        when (event) {
            PacksEvent.Refresh -> load(refresh = true)
            is PacksEvent.PackClicked -> viewModelScope.launch {
                _navigation.send(PacksNavigationEvent.GoToPackDetails(event.packId))
            }
        }
    }

    private fun load(refresh: Boolean = false) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { current ->
                when (current) {
                    is PacksUiState.Loaded -> if (refresh) current.copy(isRefreshing = true) else current
                    else -> PacksUiState.Loading
                }
            }
            getPacks().collect { result ->
                _uiState.value = when (result) {
                    is ApiResult.Success -> PacksUiState.Loaded(result.data)
                    is ApiResult.SuccessNobody -> PacksUiState.Loaded(emptyList())
                    is ApiResult.Error -> PacksUiState.Error(errorParser.parse(result.type))
                }
            }
        }
    }
}
