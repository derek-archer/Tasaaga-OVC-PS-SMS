package com.example.tasaagaovcps.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** State that a ViewModel exposes to drive [GeminiInsightCard]. */
sealed class GeminiInsightState {
    data object Idle : GeminiInsightState()
    data object Loading : GeminiInsightState()
    data class Success(val text: String) : GeminiInsightState()
    data class Error(val message: String) : GeminiInsightState()
}

/**
 * Reusable AI insight card for all dashboards.
 *
 * @param title       Card header e.g. "Attendance Insight"
 * @param state       Current [GeminiInsightState]
 * @param onRefresh   Called when the refresh icon is tapped
 * @param modifier    Optional modifier
 */
@Composable
fun GeminiInsightCard(
    title: String,
    state: GeminiInsightState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val geminiPurple = Color(0xFF7C4DFF)
    val geminiDark   = Color(0xFF1A0533)
    val geminiSurface = Color(0xFF2D1B69)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = geminiDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // ── Header row ──
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(geminiPurple, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AutoAwesome,
                            contentDescription = "AI",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Powered by Gemini",
                            fontSize = 10.sp,
                            color = geminiPurple
                        )
                    }
                }

                IconButton(
                    onClick = onRefresh,
                    enabled = state !is GeminiInsightState.Loading
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = "Refresh AI insight",
                        tint = if (state is GeminiInsightState.Loading) Color.Gray else geminiPurple
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // ── Body ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(geminiSurface, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                when (state) {
                    is GeminiInsightState.Idle -> {
                        Text(
                            text = "Tap ↻ to generate an AI insight from live school data.",
                            color = Color(0xFFB0BEC5),
                            fontSize = 13.sp
                        )
                    }

                    is GeminiInsightState.Loading -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = geminiPurple,
                                strokeWidth = 2.dp
                            )
                            PulsingText("Analysing school data…", geminiPurple)
                        }
                    }

                    is GeminiInsightState.Success -> {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(tween(400)),
                            exit = fadeOut()
                        ) {
                            Text(
                                text = state.text,
                                color = Color(0xFFE0E0E0),
                                fontSize = 13.sp,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    is GeminiInsightState.Error -> {
                        Text(
                            text = "⚠️ ${state.message}",
                            color = Color(0xFFFF5252),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PulsingText(text: String, color: Color) {
    val transition = rememberInfiniteTransition(label = "pulse")
    val alpha by transition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "textAlpha"
    )
    Text(
        text = text,
        color = color,
        fontSize = 13.sp,
        modifier = Modifier.alpha(alpha)
    )
}
