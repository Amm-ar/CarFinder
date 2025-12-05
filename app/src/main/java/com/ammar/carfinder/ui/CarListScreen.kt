package com.ammar.carfinder.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ammar.carfinder.R
import com.ammar.carfinder.data.model.Car
import com.ammar.carfinder.data.model.CarStatus
import com.ammar.carfinder.viewmodel.CarViewModel
import com.ammar.carfinder.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarListScreen(
    viewModel: CarViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel(),
    onAddCarClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val profileState by profileViewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                // Drawer content can go here
                Text("Car Finder Menu", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                NavigationDrawerItem(
                    label = { Text("Home") },
                    selected = true,
                    onClick = {
                        scope.launch { drawerState.close() }
                    }
                )
                 NavigationDrawerItem(
                    label = { Text("Profile") },
                    selected = false,
                    onClick = {
                         scope.launch { drawerState.close() }
                         onSettingsClick()
                    }
                )
                // Add more items here
            }
        }
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = onAddCarClick) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.report_car))
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Floating Search Bar with Menu and Profile
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                        
                        TextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                viewModel.searchCars(it)
                            },
                            placeholder = { Text(stringResource(R.string.search_hint)) },
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            singleLine = true
                        )

                        IconButton(
                            onClick = onSettingsClick,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        ) {
                            if (!profileState.avatarUrl.isNullOrEmpty()) {
                                AsyncImage(
                                    model = profileState.avatarUrl,
                                    contentDescription = "Profile",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Filter Chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilterChip(
                        selected = false, 
                        onClick = { viewModel.loadAllCars() },
                        label = { Text(stringResource(R.string.all)) }
                    )
                    FilterChip(
                        selected = false,
                        onClick = { viewModel.filterCars(CarStatus.LOST) },
                        label = { Text(stringResource(R.string.lost)) },
                        colors = FilterChipDefaults.filterChipColors(
                            labelColor = MaterialTheme.colorScheme.error
                        )
                    )
                    FilterChip(
                        selected = false,
                        onClick = { viewModel.filterCars(CarStatus.FOUND) },
                        label = { Text(stringResource(R.string.found)) },
                        colors = FilterChipDefaults.filterChipColors(
                            labelColor = Color(0xFF4CAF50)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Loading & Error States
                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (uiState.error != null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.error_prefix, uiState.error!!), color = MaterialTheme.colorScheme.error)
                    }
                } else {
                    // Car List
                    if (uiState.cars.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(stringResource(R.string.no_cars), style = MaterialTheme.typography.bodyLarge)
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.cars) { car ->
                                CarItem(car = car)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CarItem(car: Car) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Car Image
            if (!car.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = car.imageUrl,
                    contentDescription = "Car Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${car.make} ${car.model}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                AssistChip(
                    onClick = { },
                    label = { Text(if (car.status == CarStatus.LOST) stringResource(R.string.lost) else stringResource(R.string.found)) },
                    colors = AssistChipDefaults.assistChipColors(
                        labelColor = if (car.status == CarStatus.LOST) MaterialTheme.colorScheme.error else Color(0xFF4CAF50)
                    )
                )
            }
            if (car.year != null) {
                Text(stringResource(R.string.year_label, car.year), style = MaterialTheme.typography.bodyMedium)
            }
            if (!car.color.isNullOrBlank()) {
                Text(stringResource(R.string.color_label, car.color!!), style = MaterialTheme.typography.bodyMedium)
            }
            if (!car.licensePlate.isNullOrBlank()) {
                Text(stringResource(R.string.plate_label, car.licensePlate!!), style = MaterialTheme.typography.bodyMedium)
            }
             if (!car.chassisNumber.isNullOrBlank()) {
                Text(stringResource(R.string.chassis_label, car.chassisNumber!!), style = MaterialTheme.typography.bodyMedium)
            }
            if (!car.description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = car.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
