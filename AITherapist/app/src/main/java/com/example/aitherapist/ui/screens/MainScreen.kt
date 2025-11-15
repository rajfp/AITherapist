package com.example.aitherapist.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.alpha
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.aitherapist.ui.components.EntryCard
import com.example.aitherapist.ui.components.GradientButton
import com.example.aitherapist.ui.components.SuggestionCard
import com.example.aitherapist.ui.theme.*
import com.example.aitherapist.ui.viewmodel.MainViewModel
import com.example.aitherapist.ui.viewmodel.UIState

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    
    val scrollState = rememberScrollState()
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = androidx.compose.ui.graphics.Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            LightBackground,
                            SoftYellow.copy(alpha = 0.3f),
                            Peach.copy(alpha = 0.2f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp)
                    .padding(top = paddingValues.calculateTopPadding() + 16.dp)
                    .padding(bottom = paddingValues.calculateBottomPadding() + 80.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            // Header with emoji
            Text(
                text = "🌟 Your AI Coach & Therapist 🌟",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Reflect on your day and get personalized suggestions",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(10.dp))
            
            // Entry Cards
            EntryCard(
                title = "What I Did Today",
                emoji = "✨",
                value = uiState.whatDid,
                onValueChange = viewModel::updateWhatDid,
                placeholder = "Share what you accomplished today...",
                cardColor = CalmGreen
            )
            
            EntryCard(
                title = "What I Didn't Do",
                emoji = "📝",
                value = uiState.whatDidNot,
                onValueChange = viewModel::updateWhatDidNot,
                placeholder = "What did you plan but couldn't do?",
                cardColor = SkyBlue
            )
            
            EntryCard(
                title = "How I Felt",
                emoji = "💭",
                value = uiState.feelings,
                onValueChange = viewModel::updateFeelings,
                placeholder = "Describe your feelings and emotions...",
                cardColor = Lavender
            )
            
            // Submit Button
            val coroutineScope = rememberCoroutineScope()
            GradientButton(
                text = "Get AI Suggestions",
                emoji = "🚀",
                onClick = {
                    viewModel.submitEntry()
                    // Scroll to bottom after a short delay to show suggestions
                    coroutineScope.launch {
                        delay(500)
                        scrollState.animateScrollTo(scrollState.maxValue)
                    }
                },
                enabled = uiState.whatDid.isNotBlank() || 
                         uiState.whatDidNot.isNotBlank() || 
                         uiState.feelings.isNotBlank(),
                isLoading = uiState.isLoading
            )
            
            // Error Message
            uiState.errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "⚠️", style = MaterialTheme.typography.titleLarge)
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            
            // Suggestions Section
            if (uiState.suggestions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                
                // Animated header
                AnimatedVisibility(
                    visible = uiState.suggestions.isNotEmpty(),
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "🎯 Your Personalized Suggestions",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Here's what your AI coach suggests for tomorrow:",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(10.dp))
                
                // Suggestions List
                uiState.suggestions.forEachIndexed { index, suggestion ->
                    SuggestionCard(
                        suggestion = suggestion,
                        index = index,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                // Success message
                if (uiState.isSubmitted) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = CalmGreen.copy(alpha = 0.2f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            content = {
                                Text(
                                    text = "🎉 Great job reflecting on your day!",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        )
                    }
                }
            }
            
            // Extra bottom padding to ensure button is always accessible
            Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
    
    // Auto-scroll when suggestions appear
    LaunchedEffect(uiState.suggestions.size) {
        if (uiState.suggestions.isNotEmpty()) {
            delay(300)
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }
}

