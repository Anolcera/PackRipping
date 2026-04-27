package com.example.packripping.features.packs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packripping.core.ui.components.SnapCarousel
import com.example.packripping.core.ui.theme.PackRippingTheme
import com.example.packripping.domain.models.PackSummary
import com.example.packripping.features.packs.components.PackCarouselItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacksScreen(
    state: PacksUiState,
    onEvent: (PacksEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text("Packs") })
        },
    ) { padding ->
        when (state) {
            PacksUiState.Loading -> LoadingState(Modifier.padding(padding))
            is PacksUiState.Loaded -> PacksCarousel(
                packs = state.packs,
                onPackClicked = { onEvent(PacksEvent.PackClicked(it)) },
                contentPadding = padding,
            )
            is PacksUiState.Error -> ErrorState(
                message = state.message,
                onRetry = { onEvent(PacksEvent.Refresh) },
                modifier = Modifier.padding(padding),
            )
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Button(onClick = onRetry) { Text("Try again") }
    }
}

@Composable
private fun PacksCarousel(
    packs: List<PackSummary>,
    onPackClicked: (Int) -> Unit,
    contentPadding: PaddingValues,
) {
    if (packs.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().padding(contentPadding), contentAlignment = Alignment.Center) {
            Text("No packs available right now.")
        }
        return
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(contentPadding),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Browse and rip a sealed pack",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        )
        Spacer(Modifier.height(64.dp))
        SnapCarousel(
            items = packs,
            key = { it.id },
            itemWidth = 220.dp,
        ) { pack, scaleModifier ->
            PackCarouselItem(
                pack = pack,
                onClick = { onPackClicked(pack.id) },
                modifier = scaleModifier,
            )
        }
    }
}

private val samplePacks = listOf(
    PackSummary(
        id = 7, name = "Basketball Play Maker Pack", priceCents = 1000,
        imageUrl = "", maxValueCents = 17500,
    ),
    PackSummary(
        id = 4, name = "Pokemon Quest Pack", priceCents = 1000,
        imageUrl = "", maxValueCents = 12500,
    ),
    PackSummary(
        id = 6, name = "Basketball Starter Pack", priceCents = 500,
        imageUrl = "", maxValueCents = 8000,
    ),
)

@Preview
@Composable
private fun PacksScreenLoadedPreview() = PackRippingTheme {
    PacksScreen(state = PacksUiState.Loaded(samplePacks), onEvent = {})
}

@Preview
@Composable
private fun PacksScreenLoadingPreview() = PackRippingTheme {
    PacksScreen(state = PacksUiState.Loading, onEvent = {})
}

@Preview
@Composable
private fun PacksScreenErrorPreview() = PackRippingTheme {
    PacksScreen(state = PacksUiState.Error("No connection — check your network and try again."), onEvent = {})
}

@Preview
@Composable
private fun PacksScreenEmptyPreview() = PackRippingTheme {
    PacksScreen(state = PacksUiState.Loaded(emptyList()), onEvent = {})
}
