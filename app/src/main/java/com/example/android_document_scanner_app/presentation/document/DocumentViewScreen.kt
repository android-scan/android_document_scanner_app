package com.example.android_document_scanner_app.presentation.document

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DocumentViewScreen(
    documentId:       Long,
    onNavigateToEdit: (String) -> Unit,
    onNavigateBack:   () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("DocumentViewScreen — в разработке")
    }
}
