package com.example.app.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.app.data.entity.PasswordItem
import com.example.app.viewModels.MainViewModel
import kotlinx.coroutines.launch

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    // Collect the state from the ViewModel
    val state = viewModel.state.collectAsState().value

    // State for bottom sheet
    val bottomSheetState = rememberModalBottomSheetState()
    var currentSheet by remember { mutableStateOf<BottomSheetContent?>(null) }
    val scope = rememberCoroutineScope()

    // Show loading indicator if data is being fetched
    if (state.isLoading) {
        Box {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else if (state.error != null) {
        // Show error if there is an issue with data fetching
        Text(text = state.error, color = Color.Red)
    } else {
        currentSheet?.let { sheetContent ->
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        currentSheet = null
                    }
                },
                sheetState = bottomSheetState,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp, max = 600.dp)
            ) {
                when (sheetContent) {
                    is BottomSheetContent.Add -> {
                        AddPasswordScreen(
                            onSaveClick = { passwordItem ->
                                viewModel.upsertPassword(passwordItem)
                                scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                    currentSheet = null
                                }
                            }
                        )
                    }

                    is BottomSheetContent.Detail -> {
                        AccountDetailScreen(
                            item = sheetContent.item,
                            onDeleteClick = {
                                viewModel.deletePassword(it)
                                scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                    currentSheet = null
                                }
                            },
                            onEditClick = {
                                AddPasswordScreen(
                                    existingItem = it,
                                    onSaveClick = { passwordItem ->
                                        viewModel.upsertPassword(passwordItem)
                                        scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                            currentSheet = null
                                        }
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Password Manager") },
                    modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        currentSheet = BottomSheetContent.Add
                    },
                    containerColor = Color(0xFF2979FF),
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                HorizontalDivider(
                    modifier = Modifier.padding(bottom = 16.dp),
                    thickness = 2.dp,
                    color = Color(0xFFE0E0E0) // light gray similar to your screenshot
                )

                LazyColumn {
                   items(state.passwords, key = {it.id} ){item ->

                       PasswordItemCard(
                           item = item,
                           onClick = { currentSheet = BottomSheetContent.Detail(item) }
                       )
                   }
                }
            }
        }
    }
}


@Composable
fun PasswordItemCard(
    item: PasswordItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(50),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.Acctype,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                    modifier = Modifier.padding(end = 12.dp)
                )
                Text(
                    text = "*******",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Go to details",
                tint = Color.Gray,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

sealed class BottomSheetContent {
    object Add : BottomSheetContent()
    data class Detail(val item: PasswordItem) : BottomSheetContent()
}

