package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val systemInDark = isSystemInDarkTheme()
      var isDarkMode by rememberSaveable { mutableStateOf(systemInDark) }
      var selectedService by rememberSaveable { mutableStateOf<String?>(null) }

      MyApplicationTheme(darkTheme = isDarkMode) {
        AnimatedContent(
          targetState = selectedService,
          transitionSpec = {
            if (targetState != null) {
              slideInHorizontally { width -> width } togetherWith slideOutHorizontally { width -> -width }
            } else {
              slideInHorizontally { width -> -width } togetherWith slideOutHorizontally { width -> width }
            }
          },
          label = "screen_transition",
          modifier = Modifier.fillMaxSize()
        ) { serviceId ->
          val currentService = serviceId?.let { id ->
            BusService.entries.find { it.id == id }
          }

          if (currentService != null) {
            WebBrowserScreen(
              service = currentService,
              isDarkMode = isDarkMode,
              onToggleTheme = { isDarkMode = !isDarkMode },
              onBack = { selectedService = null }
            )
          } else {
            MainScreen(
              isDarkMode = isDarkMode,
              onToggleTheme = { isDarkMode = !isDarkMode },
              onSelectService = { service -> selectedService = service.id }
            )
          }
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
  MyApplicationTheme {
    MainScreen(
      isDarkMode = false,
      onToggleTheme = {},
      onSelectService = {}
    )
  }
}

