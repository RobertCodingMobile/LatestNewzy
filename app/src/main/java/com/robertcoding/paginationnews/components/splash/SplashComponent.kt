package com.robertcoding.paginationnews.components.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.robertcoding.paginationnews.animation.NewzyLoadingAnimation
import com.robertcoding.paginationnews.ui.theme.CardOcean
import kotlinx.coroutines.delay

// TODO: Refactor to SplashScreen API
@Composable
fun SplashScreen(onFinished: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CardOcean),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        NewzyLoadingAnimation()
        LaunchedEffect(Unit) {
            delay(1_500)
            onFinished()
        }
    }
}