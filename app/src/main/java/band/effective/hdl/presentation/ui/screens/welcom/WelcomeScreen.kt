package band.effective.hdl.presentation.ui.screens.welcom

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import band.effective.hdl.presentation.auth.AuthResultContract
import band.effective.hdl.presentation.auth.AuthResultContractState
import band.effective.hdl.presentation.ui.screens.welcom.webview.LeaderAuthScreen

@Composable
fun WelcomeScreen(viewModel : WelcomeViewModel) {
    val context = LocalContext.current
    var isShowAuth by remember { mutableStateOf(false) }

    //TODO использовать это в будещем
    val authLauncher = rememberLauncherForActivityResult(AuthResultContract()) { authState ->
        when (authState) {
            is AuthResultContractState.Error -> {
                Log.d("AUTH","NOT Ok")
                viewModel.sendEvent(
                    WelcomeUiEvent.OnAuthCodeDidNotReceived(authState.message)
                )
            }

            is AuthResultContractState.Success -> {
                Log.d("AUTH","Ok")
//                viewModel.sendEvent(WelcomeUiEvent.OnAuthCodeReceived())
            }

            null -> return@rememberLauncherForActivityResult
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect {effect ->
            when (effect) {
                is WelcomeUiEffect.OpenAuthView -> {
                    isShowAuth = true
                }
            }
        }
    }

    if(isShowAuth){
        LeaderAuthScreen {
            isShowAuth = false
            viewModel.sendEvent(
                WelcomeUiEvent.OnAuthCodeReceived(it)
            )
        }
    } else {
        Button(onClick = { viewModel.sendEvent(WelcomeUiEvent.OnClickAuth) }) {
            Text("Войти")
        }
    }
}