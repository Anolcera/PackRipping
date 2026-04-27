package com.example.packripping.di

import com.example.packripping.domain.models.Pack
import com.example.packripping.features.pack_details.PackDetailsViewModel
import com.example.packripping.features.packs.PacksViewModel
import com.example.packripping.features.pull_reveal.PullRevealViewModel
import com.example.packripping.features.pulls.PullsViewModel
import com.example.packripping.features.showroom.ShowroomViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelsModule = module {
    viewModelOf(::PacksViewModel)
    viewModel { (packId: Int) ->
        PackDetailsViewModel(
            packId = packId,
            getPackDetails = get(),
        )
    }
    viewModel { (pack: Pack) ->
        PullRevealViewModel(
            pack = pack,
            buyPack = get(),
            sellCard = get(),
            storeCard = get(),
        )
    }
    viewModelOf(::PullsViewModel)
    viewModelOf(::ShowroomViewModel)
}
