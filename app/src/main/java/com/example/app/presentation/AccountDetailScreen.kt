package com.example.app.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app.data.entity.PasswordItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailScreen(
    item: PasswordItem,
    onDeleteClick: (PasswordItem) -> Unit,
    onEditClick: @Composable (PasswordItem) -> Unit
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    var showEditScreen by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Account Details",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color(0xFF2F6FED),
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->
        if (showEditScreen) {
            onEditClick(item)
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Details Section
                Column(verticalArrangement = Arrangement.spacedBy(5.dp) ) {
                    Text(
                        text = "Account Type",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = item.Acctype,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Username/Email",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = item.username,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Password",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isPasswordVisible) item.password else "•".repeat(item.password.length),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Button(
                            onClick = { showEditScreen = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Text(
                                "Edit",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Button(
                            onClick = { onDeleteClick(item) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4C4C)),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Text(
                                "Delete",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

