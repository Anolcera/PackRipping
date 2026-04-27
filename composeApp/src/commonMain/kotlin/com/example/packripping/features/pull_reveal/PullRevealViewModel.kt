package com.example.packripping.features.pull_reveal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packripping.domain.models.Pack
import com.example.packripping.domain.usecases.BuyPackUseCase
import com.example.packripping.domain.usecases.SellCardUseCase
import com.example.packripping.domain.usecases.StoreCardUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PullRevealViewModel(
    private val pack: Pack,
    private val buyPack: BuyPackUseCase,
    private val sellCard: SellCardUseCase,
    private val storeCard: StoreCardUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<RevealState>(RevealState.Loading)
    val state = _state.asStateFlow()

    private val _navigation = Channel<PullRevealNavigationEvent>(Channel.BUFFERED)
    val navigation = _navigation.receiveAsFlow()

    init {
        rip()
    }

    fun onEvent(event: PullRevealEvent) {
        when (event) {
            PullRevealEvent.SellClicked -> finalize(store = false)
            PullRevealEvent.StoreClicked -> finalize(store = true)
            PullRevealEvent.DismissError -> rip()
        }
    }

    private fun rip() {
        _state.value = RevealState.Loading
        viewModelScope.launch {
            try {
                val card = buyPack(pack)
                _state.value = RevealState.Revealed(card)
            } catch (e: Exception) {
                _state.value = RevealState.Failed("Couldn't open the pack. Please try again.")
            }
        }
    }

    private fun finalize(store: Boolean) {
        val card = (_state.value as? RevealState.Revealed)?.card ?: return
        viewModelScope.launch {
            _state.value = RevealState.Finalizing(card)
            try {
                if (store) {
                    storeCard(card = card, packId = pack.id, packName = pack.name)
                } else {
                    sellCard(card = card, packId = pack.id, packName = pack.name)
                }
                _navigation.send(PullRevealNavigationEvent.Done)
            } catch (e: Exception) {
                _state.value = RevealState.Revealed(card)
            }
        }
    }
}
