package com.example.aitherapist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aitherapist.data.AppDatabase
import com.example.aitherapist.data.EntryRepository
import com.example.aitherapist.network.OpenAIService
import com.example.aitherapist.ui.screens.MainScreen
import com.example.aitherapist.ui.theme.AITherapistTheme
import com.example.aitherapist.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            AITherapistTheme {
                MainScreenContent()
            }
        }
    }
}

@Composable
fun MainScreenContent() {
    // Initialize dependencies - these will be remembered across recompositions
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context.applicationContext) }
    val entryDao = remember { database.entryDao() }
    val entryRepository = remember { EntryRepository(entryDao) }
    val openAIService = remember { OpenAIService() }
    
    // Create ViewModel with dependencies
    val viewModel: MainViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(entryRepository, openAIService) as T
            }
        }
    )
    
    MainScreen(
        viewModel = viewModel,
        modifier = Modifier.fillMaxSize()
    )
}