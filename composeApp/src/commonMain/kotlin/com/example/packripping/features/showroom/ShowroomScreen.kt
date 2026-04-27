package com.example.packripping.features.showroom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.packripping.core.ui.theme.PackRippingTheme
import com.example.packripping.core.ui.toDollarString
import com.example.packripping.domain.models.Card as DomainCard
import com.example.packripping.domain.models.Pull
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowroomScreen(state: ShowroomUiState, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("Showroom") }) },
    ) { padding ->
        when {
            state.isLoading -> Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                CircularProgressIndicator()
            }
            state.pulls.isEmpty() -> EmptyState(Modifier.padding(padding))
            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(state.pulls, key = { it.id }) { pull ->
                    VaultCard(pull)
                }
            }
        }
    }
}

@Composable
private fun VaultCard(pull: Pull) {
    Card {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            AsyncImage(
                model = pull.card.imageUrl,
                contentDescription = pull.card.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth().aspectRatio(0.7f),
            )
            Text(
                text = pull.card.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
            )
            Text(
                text = pull.card.sellPriceCents.toDollarString(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "${pull.card.gradingCompany} ${pull.card.grade}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
    ) {
        Text(text = "Your vault is empty", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "Stored pulls show up here. Open a pack and store something good.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private val sampleCard = DomainCard(
    sku = "BAS-A", name = "Tyrese Maxey 2020-21 Panini Prizm", imageUrl = "",
    sellPriceCents = 17500, grade = "10.0", gradingCompany = "PSA",
)

@Preview
@Composable
private fun ShowroomScreenLoadedPreview() = PackRippingTheme {
    ShowroomScreen(
        state = ShowroomUiState(
            isLoading = false,
            pulls = listOf(
                Pull("a", sampleCard, 7, "Basketball Play Maker Pack", Instant.fromEpochMilliseconds(0), isStored = true),
                Pull("b", sampleCard.copy(sku = "B", name = "David Robinson", sellPriceCents = 14000),
                    7, "Basketball Play Maker Pack", Instant.fromEpochMilliseconds(0), isStored = true),
            ),
        ),
    )
}

@Preview
@Composable
private fun ShowroomScreenEmptyPreview() = PackRippingTheme {
    ShowroomScreen(state = ShowroomUiState(isLoading = false))
}
