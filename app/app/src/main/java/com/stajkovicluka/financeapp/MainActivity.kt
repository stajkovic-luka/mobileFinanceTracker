package com.stajkovicluka.financeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.stajkovicluka.financeapp.ui.FinanceApp
import com.stajkovicluka.financeapp.ui.theme.FinanceAppTheme

// Android ulazna tacka koja prikazuje glavni Compose sadrzaj aplikacije
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinanceAppTheme {
                FinanceApp()
            }
        }
    }
}
