package com.example.shoppinglistmanager.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.shoppinglistmanager.data.entity.Store
import com.example.shoppinglistmanager.ui.common.TopAppBar
import com.example.shoppinglistmanager.ui.theme.ShoppingListManagerTheme
import com.example.shoppinglistmanager.ui.utils.showToast
import com.example.shoppinglistmanager.ui.viewmodel.OptionsViewModel
import com.example.shoppinglistmanager.ui.viewmodel.StoreViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val optionsViewModel = OptionsViewModel(application)
            ShoppingListManagerTheme(optionsViewModel) {
                val storeViewModel = StoreViewModel(application)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MapScreen(storeViewModel)
                }
            }
        }
    }
}

@Composable
fun MapScreen(storeViewModel: StoreViewModel) {
    Scaffold(
        topBar = { TopAppBar("Stores Map") }
    ) { innerPadding ->
        MapWithStoreMarkers(storeViewModel, innerPadding)
    }
}

@SuppressLint("MissingPermission")
@Composable
private fun MapWithStoreMarkers(
    storeViewModel: StoreViewModel,
    innerPadding: PaddingValues
) {
    val context: Context = LocalContext.current

    val stores: Map<String, Store> by storeViewModel.stores
        .collectAsState(initial = emptyMap())

    val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val location: Location? =
        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

    if (location == null) {
        showToast(context, "No location information available.")
        return
    }

    val latLng = LatLng(location.latitude, location.longitude)
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 10f)
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        cameraPositionState = cameraPositionState
    ) {
        stores.values.toList().forEach { store ->
            Marker(
                state = MarkerState(
                    position = LatLng(store.latitude, store.longitude)
                ),
                title = store.name,
                snippet = store.description
            )
        }
    }
}