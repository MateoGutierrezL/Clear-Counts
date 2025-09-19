package com.example.clearcounts.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class DrawerItem (
    val icon: ImageVector,
    val text: String
){
    ABOUT(Icons.Default.Info, "About"),
    SETTINGS(Icons.Default.Settings, "Settings"),
    RECENT(Icons.Default.DateRange, "Recent"),
    ACCOUNT(Icons.Default.AccountCircle, "Account"),
}