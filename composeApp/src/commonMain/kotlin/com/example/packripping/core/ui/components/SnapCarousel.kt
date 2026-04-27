package com.example.packripping.core.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.snapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlin.math.abs

@Composable
fun <T> SnapCarousel(
    items: List<T>,
    key: (T) -> Any,
    itemWidth: Dp,
    modifier: Modifier = Modifier,
    itemSpacing: Dp = 12.dp,
    focusedScale: Float = 1.05f,
    unfocusedScale: Float = 0.85f,
    itemContent: @Composable (item: T, scaleModifier: Modifier) -> Unit,
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val sidePadding = ((maxWidth - itemWidth) / 2).coerceAtLeast(16.dp)
        val listState = rememberLazyListState()
        val decayAnimationSpec = rememberSplineBasedDecay<Float>()
        val flingBehavior = remember(listState, decayAnimationSpec) {
            snapFlingBehavior(
                snapLayoutInfoProvider = SnapLayoutInfoProvider(
                    lazyListState = listState,
                    snapPosition = SnapPosition.Center,
                ),
                decayAnimationSpec = decayAnimationSpec,
                snapAnimationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
            )
        }
        LazyRow(
            state = listState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(horizontal = sidePadding, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(itemSpacing),
        ) {
            items(items, key = key) { item ->
                val itemKey = key(item)
                val scaleModifier = Modifier.graphicsLayer {
                    val info = listState.layoutInfo
                    val itemInfo = info.visibleItemsInfo.firstOrNull { it.key == itemKey }
                    val scale = if (itemInfo == null) {
                        unfocusedScale
                    } else {
                        val viewportCenter =
                            (info.viewportStartOffset + info.viewportEndOffset) / 2f
                        val itemCenter = itemInfo.offset + itemInfo.size / 2f
                        val maxDistance =
                            (info.viewportEndOffset - info.viewportStartOffset) / 2f
                        val t = (abs(itemCenter - viewportCenter) / maxDistance)
                            .coerceIn(0f, 1f)
                        lerp(focusedScale, unfocusedScale, t)
                    }
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = TransformOrigin.Center
                }
                itemContent(item, scaleModifier)
            }
        }
    }
}
