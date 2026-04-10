package com.example.queuesense.ui.screens.forecast

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.queuesense.data.model.QueueLocation
import com.example.queuesense.ui.components.StatusBadge
import com.example.queuesense.viewmodel.QueueViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun ForecastScreen(viewModel: QueueViewModel) {
    val locations by viewModel.locations.collectAsState()
    val userLocation by viewModel.userLocation.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    // Default coordinates (used only if location is not yet detected)
    val defaultPos = LatLng(14.6819, 77.6006) 
    
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation ?: defaultPos, 15f)
    }

    var selectedLocation by remember { mutableStateOf<QueueLocation?>(null) }

    // Sync camera with user location when it becomes available for the first time
    var initialLocationSet by remember { mutableStateOf(false) }
    LaunchedEffect(userLocation) {
        if (userLocation != null && !initialLocationSet) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(userLocation!!, 15f)
            initialLocationSet = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = true, // Shows the blue dot
                mapType = MapType.NORMAL
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true, // Shows +/- buttons
                compassEnabled = true,      // Shows compass
                myLocationButtonEnabled = true, // Shows the native 'center on me' button
                mapToolbarEnabled = true    // Shows native navigation icons when marker clicked
            ),
            onMapClick = { selectedLocation = null }
        ) {
            locations.forEach { location ->
                Marker(
                    state = MarkerState(position = LatLng(location.latitude, location.longitude)),
                    title = location.name,
                    onClick = {
                        selectedLocation = location
                        false // return false to use standard Google Maps marker behavior
                    }
                )
            }
        }

        // Info Card for Selected Location (Standard bottom overlay style)
        selectedLocation?.let { location ->
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = location.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "${location.category} • ${location.city}",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                        StatusBadge(location.status)
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Estimated Wait: ${location.waitTime}",
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Only show loader if we have no data yet
        if (isLoading && locations.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }
    }
}
