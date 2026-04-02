package com.example.queuesense.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StatusBadge(status: String) {
    val badgeColor = when (status.lowercase()) {
        "short" -> Color(0xFF39FF14)
        "moderate" -> Color(0xFFFFEA00)
        "long" -> Color(0xFFFF9800)
        else -> Color.LightGray
    }

    Text(
        text = status,
        color = Color.Black,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .background(badgeColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}