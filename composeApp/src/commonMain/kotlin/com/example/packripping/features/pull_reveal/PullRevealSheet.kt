package com.example.packripping.features.pull_reveal

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.packripping.core.ui.theme.PackRippingTheme
import com.example.packripping.core.ui.toDollarString
import com.example.packripping.domain.models.Card

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullRevealSheet(
    state: RevealState,
    onEvent: (PullRevealEvent) -> Unit,
    onDismissRequest: () -> Unit,
) {
    // Block dismiss while a buy or finalize is in flight; allow it once a card
    // is revealed (the sell/store buttons are the intended exit, but a drag-down
    // is acceptable too — the user can re-open the pack if they change their mind).
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { state !is RevealState.Loading && state !is RevealState.Finalizing },
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        AnimatedContent(
            targetState = state,
            modifier = Modifier
                .fillMaxWidth()
                .height(560.dp),
            contentKey = { it.contentKey() },
            transitionSpec = {
                (fadeIn(animationSpec = tween(450)) +
                    scaleIn(initialScale = 0.85f, animationSpec = tween(450)))
                    .togetherWith(
                        fadeOut(animationSpec = tween(200)) +
                            scaleOut(targetScale = 1.1f, animationSpec = tween(200))
                    )
            },
            label = "reveal-content",
        ) { target ->
            when (target) {
                RevealState.Loading -> MysteryCardContent()
                is RevealState.Revealed -> RevealedCardContent(
                    card = target.card,
                    isProcessing = false,
                    onSell = { onEvent(PullRevealEvent.SellClicked) },
                    onStore = { onEvent(PullRevealEvent.StoreClicked) },
                )
                is RevealState.Finalizing -> RevealedCardContent(
                    card = target.card,
                    isProcessing = true,
                    onSell = {},
                    onStore = {},
                )
                is RevealState.Failed -> FailedContent(
                    message = target.message,
                    onRetry = { onEvent(PullRevealEvent.DismissError) },
                )
            }
        }
    }
}

private fun RevealState.contentKey(): Any = when (this) {
    RevealState.Loading -> "loading"
    is RevealState.Revealed -> "revealed:${card.sku}"
    is RevealState.Finalizing -> "finalizing:${card.sku}"
    is RevealState.Failed -> "failed"
}

@Composable
private fun MysteryCardContent() {
    val transition = androidx.compose.animation.core.rememberInfiniteTransition(label = "mystery-pulse")
    val pulse by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse",
    )
    val tilt by transition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "tilt",
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text(
            text = "Ripping…",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Card(
            modifier = Modifier
                .width(220.dp)
                .aspectRatio(0.7f)
                .graphicsLayer {
                    scaleX = pulse
                    scaleY = pulse
                    rotationZ = tilt
                }
                .shadow(elevation = 18.dp, shape = RoundedCornerShape(16.dp), clip = false),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(16.dp),
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "?",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Black,
                )
            }
        }
        Text(
            text = "Picking your card from the pack…",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun RevealedCardContent(
    card: Card,
    isProcessing: Boolean,
    onSell: () -> Unit,
    onStore: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "You pulled!",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        AsyncImage(
            model = card.imageUrl,
            contentDescription = card.name,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxHeight(0.55f)
                .fillMaxWidth(),
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = card.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "${card.gradingCompany} ${card.grade}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Button(
                onClick = onSell,
                enabled = !isProcessing,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text("Sell now for ${card.sellPriceCents.toDollarString()}")
                }
            }
            OutlinedButton(
                onClick = onStore,
                enabled = !isProcessing,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Store in Vault")
            }
        }
    }
}

@Composable
private fun FailedContent(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Button(onClick = onRetry) { Text("Try again") }
    }
}

private val sampleCard = Card(
    sku = "BAS-TYRESE-MAXEY-PSA10-10",
    name = "Tyrese Maxey 2020-21 Panini Prizm",
    imageUrl = "",
    sellPriceCents = 17500,
    grade = "10.0",
    gradingCompany = "PSA",
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PullRevealSheetLoadingPreview() = PackRippingTheme {
    PullRevealSheet(state = RevealState.Loading, onEvent = {}, onDismissRequest = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PullRevealSheetRevealedPreview() = PackRippingTheme {
    PullRevealSheet(state = RevealState.Revealed(sampleCard), onEvent = {}, onDismissRequest = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PullRevealSheetFailedPreview() = PackRippingTheme {
    PullRevealSheet(
        state = RevealState.Failed("Couldn't open the pack. Please try again."),
        onEvent = {},
        onDismissRequest = {},
    )
}
