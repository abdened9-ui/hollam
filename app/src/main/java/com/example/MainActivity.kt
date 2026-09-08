package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ui.auth.LoginScreen
import com.example.ui.navigation.HollamGestureNavigation
import com.example.ui.theme.HollamBlack
import com.example.ui.theme.HollamTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      HollamTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = HollamBlack
        ) {
          val isInitiallyLoggedIn = remember {
            try {
              if (FirebaseApp.getApps(this@MainActivity).isNotEmpty()) {
                FirebaseAuth.getInstance().currentUser != null
              } else {
                false
              }
            } catch (_: Throwable) {
              false
            }
          }

          var isLoggedIn by remember { mutableStateOf(isInitiallyLoggedIn) }

          Crossfade(
            targetState = isLoggedIn,
            animationSpec = tween(400),
            label = "auth_crossfade"
          ) { loggedIn ->
            if (loggedIn) {
              HollamGestureNavigation(
                onLogout = {
                  isLoggedIn = false
                }
              )
            } else {
              LoginScreen(
                onLoginSuccess = {
                  isLoggedIn = true
                }
              )
            }
          }
        }
      }
    }
  }
}


