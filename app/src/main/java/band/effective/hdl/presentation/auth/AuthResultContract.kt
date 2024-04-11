package band.effective.hdl.presentation.auth

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.browser.customtabs.CustomTabsIntent
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest

internal class AuthResultContract :
    ActivityResultContract<AuthorizationRequest, AuthResultContractState?>() {

    override fun createIntent(context: Context, input: AuthorizationRequest): Intent {
        val service = AuthorizationService(context)
        val customTabsIntent = CustomTabsIntent.Builder().build()
        return service.getAuthorizationRequestIntent(input, customTabsIntent)
    }

    override fun parseResult(
        resultCode: Int,
        intent: Intent?
    ): AuthResultContractState? {
        if (intent == null) return null
        val exception = AuthorizationException.fromIntent(intent)
        val tokenExchangeRequest = AuthorizationResponse.fromIntent(intent)
            ?.createTokenExchangeRequest()
        return when {
            exception != null -> {
                AuthResultContractState.Error(
                    exception.localizedMessage ?: "Unknown auth error"
                )
            }
            tokenExchangeRequest != null -> AuthResultContractState.Success(tokenExchangeRequest)
            else -> null
        }
    }
}

internal sealed class AuthResultContractState {
    class Success(val tokenRequest: TokenRequest) : AuthResultContractState()
    class Error(val message: String) : AuthResultContractState()
}
