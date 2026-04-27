package com.example.packripping.features.pulls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.packripping.core.ui.toShortLabel
import com.example.packripping.domain.models.Card as DomainCard
import com.example.packripping.domain.models.Pull
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullsScreen(state: PullsUiState, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Pulls") },
                actions = {
                    Text(
                        text = "Wallet ${state.walletCents.toDollarString()}",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                },
            )
        },
    ) { padding ->
        when {
            state.isLoading -> Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                CircularProgressIndicator()
            }
            state.pulls.isEmpty() -> EmptyState(Modifier.padding(padding))
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(state.pulls, key = { it.id }) { pull ->
                    PullRow(pull)
                }
            }
        }
    }
}

@Composable
private fun PullRow(pull: Pull) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = pull.card.imageUrl,
                contentDescription = pull.card.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(width = 56.dp, height = 80.dp),
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = pull.card.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                )
                Text(
                    text = pull.packName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = pull.pulledAt.toShortLabel(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (pull.isStored) "Stored" else "Sold ${pull.card.sellPriceCents.toDollarString()}",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (pull.isStored) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.secondary
                    },
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = "${pull.card.gradingCompany} ${pull.card.grade}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
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
        Text(
            text = "No pulls yet",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = "Tap a pack on the Packs tab and rip your first one.",
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

private val samplePulls = listOf(
    Pull("a", sampleCard, 7, "Basketball Play Maker Pack", Instant.fromEpochMilliseconds(0), isStored = true),
    Pull("b", sampleCard.copy(sku = "B", name = "Coby White 2019-20 Panini Chronicles", sellPriceCents = 850),
        7, "Basketball Play Maker Pack", Instant.fromEpochMilliseconds(0), isStored = false),
)

@Preview
@Composable
private fun PullsScreenLoadedPreview() = PackRippingTheme {
    PullsScreen(state = PullsUiState(pulls = samplePulls, walletCents = 850, isLoading = false))
}

@Preview
@Composable
private fun PullsScreenEmptyPreview() = PackRippingTheme {
    PullsScreen(state = PullsUiState(isLoading = false))
}

@Preview
@Composable
private fun PullsScreenLoadingPreview() = PackRippingTheme {
    PullsScreen(state = PullsUiState(isLoading = true))
}
