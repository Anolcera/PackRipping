package com.example.packripping.features.pack_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.packripping.core.ui.toDollarString
import com.example.packripping.domain.models.RecentPull

@Composable
fun RecentPullsRow(pulls: List<RecentPull>) {
    if (pulls.isEmpty()) return
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Recent pulls",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(pulls, key = { it.id }) { pull ->
                RecentPullCell(pull)
            }
        }
    }
}

@Composable
private fun RecentPullCell(pull: RecentPull) {
    Card(modifier = Modifier.width(110.dp)) {
        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            AsyncImage(
                model = pull.cardImageUrl,
                contentDescription = pull.cardName,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth().aspectRatio(0.7f),
            )
            Text(
                text = pull.cardName,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
            )
            Text(
                text = pull.valueCents.toDollarString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = pull.tierName,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
