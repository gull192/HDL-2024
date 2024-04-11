package band.effective.hdl.presentation.ui.screens.welcom

import net.openid.appauth.AuthorizationRequest

sealed interface WelcomeUiEvent {
    data object OnClickAuth : WelcomeUiEvent

    data class OnAuthCodeDidNotReceived(val message: String) : WelcomeUiEvent

    data class OnAuthCodeReceived(/*val tokenRequest: TokenRequest */ val code: String) : WelcomeUiEvent
}

sealed interface WelcomeUiEffect {
    data class OpenAuthView (val request: AuthorizationRequest) : WelcomeUiEffect
}