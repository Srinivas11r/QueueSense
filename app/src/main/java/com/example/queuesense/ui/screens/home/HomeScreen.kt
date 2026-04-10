package com.example.queuesense.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.queuesense.data.model.QueueLocation
import com.example.queuesense.viewmodel.QueueViewModel
import com.example.queuesense.ui.components.CategoryItem
import com.example.queuesense.ui.components.QueueCard
import com.example.queuesense.ui.components.SearchBar

@Composable
fun HomeScreen(
    viewModel: QueueViewModel,
    onLocationClick: (QueueLocation) -> Unit,
    onReportClick: (QueueLocation) -> Unit
) {
    val locations by viewModel.locations.collectAsState()
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredLocations = remember(locations, selectedCategory, searchQuery) {
        locations.filter { loc ->
            val categoryMatch = selectedCategory == null || loc.category.equals(selectedCategory, ignoreCase = true)
            val searchMatch = searchQuery.isEmpty() || loc.name.contains(searchQuery, ignoreCase = true) || loc.city.contains(searchQuery, ignoreCase = true)
            categoryMatch && searchMatch
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "QueueSense",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        text = "Smart Queue Intelligence",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                
                // Secret Seed Button (For Developer Use)
                IconButton(onClick = { viewModel.seedDatabase() }) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color.LightGray.copy(alpha = 0.5f))
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Categories",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                if (selectedCategory != null) {
                    TextButton(onClick = { selectedCategory = null }) {
                        Text("Clear")
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CategoryItem(
                    title = "Govt",
                    icon = Icons.Default.AccountBalance,
                    color = if (selectedCategory == "Govt") Color(0xFF2196F3) else Color(0xFF2196F3).copy(alpha = 0.6f),
                    onClick = { selectedCategory = "Govt" }
                )
                CategoryItem(
                    title = "Health",
                    icon = Icons.Default.LocalHospital,
                    color = if (selectedCategory == "Health") Color(0xFF460319) else Color(0xFFE91E63).copy(alpha = 0.6f),
                    onClick = { selectedCategory = "Health" }
                )
                CategoryItem(
                    title = "Banks",
                    icon = Icons.Default.AccountBalanceWallet,
                    color = if (selectedCategory == "Banks") Color(0xFF4CAF50) else Color(0xFF4CAF50).copy(alpha = 0.6f),
                    onClick = { selectedCategory = "Banks" }
                )
                CategoryItem(
                    title = "Food",
                    icon = Icons.Default.Restaurant,
                    color = if (selectedCategory == "Food") Color(0xFFFF9800) else Color(0xFFFF9800).copy(alpha = 0.6f),
                    onClick = { selectedCategory = "Food" }
                )
            }
        }

        item {
            Text(
                text = if (searchQuery.isNotEmpty()) "Search Results" else if (selectedCategory == null) "Nearest Queues" else "$selectedCategory Queues",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        if (filteredLocations.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.LocationOff,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No queues found matching your criteria.",
                            textAlign = TextAlign.Center,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        } else {
            items(filteredLocations) { loc ->
                QueueCard(
                    location = loc,
                    onDetails = { onLocationClick(loc) },
                    onReport = { onReportClick(loc) }
                )
            }
        }
    }
}
