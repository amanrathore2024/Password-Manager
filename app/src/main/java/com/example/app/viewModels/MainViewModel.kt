package com.example.app.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.entity.PasswordItem
import com.example.app.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _state = MutableStateFlow(AppState())
    val state: StateFlow<AppState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                repository.getPasswordItems().collect { items ->
                    Log.d("ViewModel", "Received ${items.size} items in ViewModel")
                    _state.value = _state.value.copy(
                        passwords = items,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun upsertPassword(passwordItem: PasswordItem) {
        viewModelScope.launch {
            repository.upsertItem(passwordItem)
        }
    }

    fun deletePassword(passwordItem: PasswordItem) {
        viewModelScope.launch {
            repository.deleteItem(passwordItem)
        }
    }
}


data class AppState(
    val isLoading: Boolean = false,
    val passwords: List<PasswordItem> = emptyList(),
    val error: String? = null
)
