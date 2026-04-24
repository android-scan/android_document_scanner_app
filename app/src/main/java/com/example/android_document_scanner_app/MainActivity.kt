package com.example.android_document_scanner_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import dagger.hilt.android.AndroidEntryPoint
import com.example.android_document_scanner_app.presentation.theme.DocScannerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DocScannerTheme {
                // TODO: заменить на AppNavGraph когда будет готов
                Text("DocScanner")
            }
        }
    }
}
