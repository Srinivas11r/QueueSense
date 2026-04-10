package com.example.queuesense.ui.screens.forecast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.queuesense.data.model.OperationalStatus
import com.example.queuesense.data.model.QueueLocation
import com.example.queuesense.ui.components.OperationalStatusBadge
import com.example.queuesense.ui.components.StatusBadge
import com.example.queuesense.viewmodel.QueueViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*

@Composable
fun ForecastScreen(viewModel: QueueViewModel) {
    val locations by viewModel.locations.collectAsState()
    val userLocation by viewModel.userLocation.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    val scope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf<QueueLocation?>(null) }
    
    val filteredLocations = remember(locations, searchQuery) {
        if (searchQuery.isBlank()) locations 
        else locations.filter { 
            it.name.contains(searchQuery, ignoreCase = true) || 
            it.city.contains(searchQuery, ignoreCase = true) ||
            it.category.contains(searchQuery, ignoreCase = true)
        }
    }

    // Default coordinates (used only if location is not yet detected)
    val defaultPos = LatLng(14.6819, 77.6006) 
    
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation ?: defaultPos, 12f)
    }

    // Sync camera with user location when it becomes available for the first time
    var initialLocationSet by remember { mutableStateOf(false) }
    LaunchedEffect(userLocation) {
        if (userLocation != null && !initialLocationSet) {
            scope.launch {
                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(userLocation!!, 14f))
            }
            initialLocationSet = true
        }
    }

    // Handle Search Zoom
    LaunchedEffect(filteredLocations) {
        if (filteredLocations.isNotEmpty() && searchQuery.isNotBlank()) {
            scope.launch {
                val boundsBuilder = LatLngBounds.builder()
                filteredLocations.forEach { boundsBuilder.include(LatLng(it.latitude, it.longitude)) }
                val bounds = boundsBuilder.build()
                cameraPositionState.animate(CameraUpdateFactory.newLatLngBounds(bounds, 150))
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = true,
                mapType = MapType.NORMAL
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                compassEnabled = true,
                myLocationButtonEnabled = true,
                mapToolbarEnabled = true
            ),
            onMapClick = { selectedLocation = null }
        ) {
            filteredLocations.forEach { location ->
                Marker(
                    state = MarkerState(position = LatLng(location.latitude, location.longitude)),
                    title = location.name,
                    snippet = "${location.category} - ${location.status}",
                    onClick = {
                        selectedLocation = location
                        scope.launch {
                            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 16f))
                        }
                        true
                    }
                )
            }
        }

        // Search Bar Overlay
        Card(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search city, office, or category...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                singleLine = true
            )
        }

        // Info Card for Selected Location
        selectedLocation?.let { location ->
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = location.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "${location.category} • ${location.city}", color = Color.Gray, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                OperationalStatusBadge(location.getOperationalStatus())
                            }
                        }
                        IconButton(
                            onClick = { selectedLocation = null },
                            modifier = Modifier.size(24.dp).clip(CircleShape).background(Color(0xFFEEEEEE))
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp))
                        }
                    }
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = Color.LightGray)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        MapInfoItem("Crowd Status", location.status, Color(0xFF6200EE))
                        MapInfoItem("Wait Time", location.waitTime, Color(0xFF03DAC5))
                        MapInfoItem("People", "${location.peopleCount}", Color(0xFF018786))
                    }
                }
            }
        }

        if (isLoading && locations.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun MapInfoItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
