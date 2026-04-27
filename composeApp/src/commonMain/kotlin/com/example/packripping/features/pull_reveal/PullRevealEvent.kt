package com.example.packripping.features.pull_reveal

sealed interface PullRevealEvent {
    data object SellClicked : PullRevealEvent
    data object StoreClicked : PullRevealEvent
    data object DismissError : PullRevealEvent
}

sealed interface PullRevealNavigationEvent {
    data object Done : PullRevealNavigationEvent
}
