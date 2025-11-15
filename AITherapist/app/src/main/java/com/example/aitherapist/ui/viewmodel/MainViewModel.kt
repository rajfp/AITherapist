package com.example.aitherapist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aitherapist.data.DailyEntry
import com.example.aitherapist.data.EntryRepository
import com.example.aitherapist.network.OpenAIService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UIState(
    val whatDid: String = "",
    val whatDidNot: String = "",
    val feelings: String = "",
    val suggestions: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSubmitted: Boolean = false
)

class MainViewModel(
    private val entryRepository: EntryRepository,
    private val openAIService: OpenAIService
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()
    
    fun updateWhatDid(text: String) {
        _uiState.value = _uiState.value.copy(whatDid = text)
    }
    
    fun updateWhatDidNot(text: String) {
        _uiState.value = _uiState.value.copy(whatDidNot = text)
    }
    
    fun updateFeelings(text: String) {
        _uiState.value = _uiState.value.copy(feelings = text)
    }
    
    fun submitEntry() {
        val currentState = _uiState.value
        if (currentState.whatDid.isBlank() && currentState.whatDidNot.isBlank() && currentState.feelings.isBlank()) {
            _uiState.value = currentState.copy(errorMessage = "Please fill in at least one field")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, errorMessage = null)
            
            try {
                // Save entry to database
                val entry = DailyEntry(
                    whatDid = currentState.whatDid,
                    whatDidNot = currentState.whatDidNot,
                    feelings = currentState.feelings
                )
                entryRepository.insertEntry(entry)
                
                // Get AI suggestions
                val result = openAIService.getSuggestions(
                    currentState.whatDid,
                    currentState.whatDidNot,
                    currentState.feelings
                )
                
                result.getOrElse { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to get suggestions: ${error.message}",
                        suggestions = emptyList()
                    )
                    return@launch
                }.let { suggestions ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        suggestions = suggestions,
                        isSubmitted = true,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "An error occurred: ${e.message}",
                    suggestions = emptyList()
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    fun resetForm() {
        _uiState.value = UIState()
    }
}

