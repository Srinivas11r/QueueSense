package com.example.queuesense.ui.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.queuesense.data.model.OperationalStatus
import com.example.queuesense.data.model.Review
import com.example.queuesense.ui.components.OperationalStatusBadge
import com.example.queuesense.ui.components.StatusBadge
import com.example.queuesense.viewmodel.QueueViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailsScreen(
    viewModel: QueueViewModel,
    locationId: String,
    onBack: () -> Unit,
    onReportClick: () -> Unit
) {
    val locations by viewModel.locations.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val location = locations.find { it.id == locationId }

    var rating by remember { mutableIntStateOf(0) }
    var comment by remember { mutableStateOf("") }

    LaunchedEffect(locationId) {
        viewModel.fetchReviews(locationId)
    }

    if (location == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(location.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                Text(location.city, color = Color.Gray, fontSize = 16.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(location.category, color = Color.Gray, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    OperationalStatusBadge(location.getOperationalStatus())
                                }
                            }
                            StatusBadge(location.status)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            DetailInfoItem(Icons.Default.Timer, "Wait Time", location.waitTime)
                            DetailInfoItem(Icons.Default.People, "People", "${location.peopleCount}")
                            DetailInfoItem(Icons.Default.Schedule, "Hours", "${location.openTime} - ${location.closeTime}")
                        }
                        
                        if (location.getOperationalStatus() == OperationalStatus.ON_BREAK) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Note: Staff is currently on Lunch Break (${location.breakStart} - ${location.breakEnd})",
                                color = Color(0xFFFF9800),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onReportClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Live Report Crowd Status", modifier = Modifier.padding(8.dp))
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text("Rate & Review", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            for (i in 1..5) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (i <= rating) Color(0xFFFFB300) else Color.LightGray,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clickable { rating = i }
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = comment,
                            onValueChange = { comment = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Share your experience...") },
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                viewModel.addReview(locationId, rating, comment)
                                rating = 0
                                comment = ""
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = rating > 0 && comment.isNotBlank(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Post Review")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("Recent Reviews", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                
                if (reviews.isEmpty()) {
                    Text(
                        "No reviews yet. Be the first to review!",
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color.Gray
                    )
                }
            }

            items(reviews) { review ->
                ReviewItem(review)
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun DetailInfoItem(icon: ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ReviewItem(review: Review) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(32.dp).clip(CircleShape).background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(review.userName.take(1), fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(review.userName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Row {
                        for (i in 1..5) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = if (i <= review.rating) Color(0xFFFFB300) else Color.LightGray,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(review.comment, fontSize = 14.sp, color = Color.DarkGray)
        }
    }
}
