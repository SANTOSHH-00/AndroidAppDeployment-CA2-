package com.example.androidappdeploymentca2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidappdeploymentca2.ui.theme.AndroidAppDeploymentCA2Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class FlightViewModel : ViewModel() {

    private val _events = MutableSharedFlow<String>(
        extraBufferCapacity = 10
    )
    val events = _events.asSharedFlow()

    fun startFlightUpdates() {
        viewModelScope.launch {
            val messages = listOf(
                "Boarding Started",
                "Gate Changed",
                "Flight Delayed",
                "Final Boarding Call"
            )

            messages.forEach {
                delay(3000)
                _events.emit(it)
            }
        }
    }
}

class CA2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidAppDeploymentCA2Theme {
                FlightNotificationScreen()
            }
        }
    }
}

@Composable
fun FlightNotificationScreen(
    vm: FlightViewModel = viewModel()
) {
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(vm) {
        vm.events.collect { message ->
            snackbar.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbar)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Flight Notifications",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    vm.startFlightUpdates()
                }
            ) {
                Text("Start Flight Updates")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlightNotificationScreenPreview() {
    AndroidAppDeploymentCA2Theme {
        FlightNotificationScreen()
    }
}
