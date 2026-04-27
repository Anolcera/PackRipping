package com.example.packripping.features.packs.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.packripping.core.ui.theme.PackRippingTheme
import com.example.packripping.core.ui.toDollarString
import com.example.packripping.domain.models.PackSummary

@Composable
fun PackCarouselItem(
    pack: PackSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.width(220.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.75f),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    model = pack.imageUrl,
                    contentDescription = pack.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Text(
                text = pack.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                minLines = 2,
                maxLines = 2,
            )
            Text(
                text = pack.priceCents.toDollarString(),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "Max value " + pack.maxValueCents.toDollarString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PackCarouselItemPreview() {
    PackRippingTheme {
        PackCarouselItem(
            pack = PackSummary(
                id = 7,
                name = "Basketball Play Maker Pack",
                priceCents = 1000,
                imageUrl = "",
                maxValueCents = 17500,
            ),
            onClick = {},
        )
    }
}
