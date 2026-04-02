package com.example.queuesense.ui.screens.forecast

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.queuesense.viewmodel.QueueViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun ForecastScreen(viewModel: QueueViewModel) {
    val locations by viewModel.locations.collectAsState()
    val userLocation by viewModel.userLocation.collectAsState()
    
    val defaultPos = LatLng(12.9716, 77.5946) // Default to Bangalore
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation ?: defaultPos, 12f)
    }

    // Update camera position when user location is available
    LaunchedEffect(userLocation) {
        userLocation?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 14f)
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = userLocation != null),
        uiSettings = MapUiSettings(zoomControlsEnabled = true)
    ) {
        locations.forEach { location ->
            Marker(
                state = MarkerState(position = LatLng(location.latitude, location.longitude)),
                title = location.name,
                snippet = "${location.category} - ${location.waitTime} wait"
            )
        }
    }
}
