package com.example.aitask3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.aitask3.ui.circle_field_view.CircleFieldComposable
import com.example.aitask3.ui.theme.AITask3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AITask3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                ) {
                    CircleFieldComposable(
                        Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}