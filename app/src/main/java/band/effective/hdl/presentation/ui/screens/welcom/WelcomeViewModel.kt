package band.effective.hdl.presentation.ui.screens.welcom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import band.effective.hdl.domain.LeaderIdRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val leaderIdRepository: LeaderIdRepository
): ViewModel() {

    private val _effect = MutableSharedFlow<WelcomeUiEffect>()
    val effect = _effect.asSharedFlow()

    fun sendEvent(event: WelcomeUiEvent) {
        when(event) {
            WelcomeUiEvent.OnClickAuth -> {
                viewModelScope.launch {
                    val request = leaderIdRepository.getRequestToken()
                    _effect.emit(WelcomeUiEffect.OpenAuthView(request))
                }
            }

            is WelcomeUiEvent.OnAuthCodeDidNotReceived -> {
                Log.e("VIEWMODEL", event.message)
            }

            is WelcomeUiEvent.OnAuthCodeReceived -> {
                viewModelScope.launch {
//                   runCatching {
//                       leaderIdRepository.performeToken(event.tokenRequest)
//                   }.onSuccess {
                       leaderIdRepository.auth(event.code)
//                   }
                }
            }
        }
    }
}