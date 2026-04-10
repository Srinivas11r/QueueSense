package com.example.queuesense.ui.screens.report

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.queuesense.viewmodel.QueueViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    viewModel: QueueViewModel,
    locationId: String,
    onBack: () -> Unit
) {
    val locations by viewModel.locations.collectAsState()
    val location = locations.find { it.id == locationId }

    var selectedLevel by remember { mutableStateOf<String?>(null) }
    var estimatedWaitTime by remember { mutableStateOf("") }
    var peopleCount by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    val crowdLevels = listOf(
        CrowdLevel("Short", "1-10 people", Color(0xFF4CAF50)),
        CrowdLevel("Moderate", "10-30 people", Color(0xFFFFC107)),
        CrowdLevel("Long", "30-50 people", Color(0xFFFF9800)),
        CrowdLevel("Very Long", "50+ people", Color(0xFFF44336))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Report Crowd Status", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            location?.let {
                Text(
                    text = it.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(text = "Help others by providing real-time updates", color = Color.Gray, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Current Crowd Level", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))

            crowdLevels.forEach { level ->
                CrowdLevelItem(
                    level = level,
                    isSelected = selectedLevel == level.title,
                    onClick = { selectedLevel = level.title }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Estimated Wait Time", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = estimatedWaitTime,
                onValueChange = { estimatedWaitTime = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. 15 mins") },
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Approx. People Count", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = peopleCount,
                onValueChange = { if (it.all { char -> char.isDigit() }) peopleCount = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. 25") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    isSubmitting = true
                    viewModel.submitReport(
                        locationId = locationId,
                        status = selectedLevel ?: "Moderate",
                        waitTime = if (estimatedWaitTime.isEmpty()) "Unknown" else estimatedWaitTime,
                        peopleCount = peopleCount.toIntOrNull() ?: 0
                    )
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = selectedLevel != null && !isSubmitting
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Submit Real-time Report", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

data class CrowdLevel(val title: String, val description: String, val color: Color)

@Composable
fun CrowdLevelItem(level: CrowdLevel, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) level.color.copy(alpha = 0.1f) else Color.White
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, level.color) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(level.color, CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(level.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(level.description, fontSize = 14.sp, color = Color.Gray)
            }
            if (isSelected) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = level.color)
            }
        }
    }
}
