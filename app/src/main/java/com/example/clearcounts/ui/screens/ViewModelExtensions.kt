package com.example.clearcounts.ui.screens

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


fun FirebaseAuth.userIdFlow(): StateFlow<String> {
    val flow = MutableStateFlow(currentUser?.uid ?: "")
    addAuthStateListener { auth ->
        (flow as MutableStateFlow).value = auth.currentUser?.uid ?: ""
    }
    return flow
}