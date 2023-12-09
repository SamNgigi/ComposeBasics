package com.example.composebasics

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.runtime.Composable

interface RallyDestination{
    val icon: ImageVector
    val route: String
    val screen: @Composable () -> Unit
}