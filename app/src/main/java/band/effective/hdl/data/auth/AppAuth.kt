package band.effective.hdl.data.auth

import android.content.Context
import android.net.Uri
import band.effective.hdl.BuildConfig
import band.effective.hdl.domain.PreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientSecretPost
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.GrantTypeValues
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.TokenRequest
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class AppAuth @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesRepository: PreferencesRepository
) {

    suspend fun getAuthRequest(): AuthorizationRequest {
        val config = createAuthConfig()
        return AuthorizationRequest.Builder(
            config,
            BuildConfig.AUTH_CLIENT_ID,
            "code",
            Uri.parse(BuildConfig.AUTH_REDIRECT_URL)
        )
            .setScope("openid leaderid offline_access")
            .setNonce(null)
            .build()
    }

    suspend fun performTokenRequest(tokenRequest: TokenRequest) {
        val tokens = suspendCoroutine { continuation ->
            val authService = AuthorizationService(context)
            val clientSecret = ClientSecretPost(BuildConfig.AUTH_CLIENT_SECRET)
            authService.performTokenRequest(tokenRequest, clientSecret) { response, ex ->
                when {
                    ex != null -> continuation.resumeWith(Result.failure(ex))
                    response != null -> {
                        continuation.resumeWith(Result.success(response))
                    }
                    else -> {
                        continuation.resumeWith(Result.failure(Throwable("Token request error")))
                    }
                }
            }
        }
        with(preferencesRepository) {
            changeAuthTokens(
                tokens.accessToken.orEmpty(),
                tokens.refreshToken.orEmpty()
            )
        }
    }

    suspend fun getRefreshTokenRequest(): TokenRequest {
        val config = createAuthConfig()
        val refreshToken = preferencesRepository.refreshToken.first()
        return TokenRequest.Builder(
            config,
            BuildConfig.AUTH_CLIENT_ID
        )
            .setGrantType(GrantTypeValues.REFRESH_TOKEN)
            .setRefreshToken(refreshToken)
            .setNonce(null)
            .build()
    }

    private suspend fun createAuthConfig(): AuthorizationServiceConfiguration {
        return AuthorizationServiceConfiguration(
            Uri.parse(BuildConfig.AUTH_SERVER_URL),
            Uri.parse("https://apps.leader-id.ru/api/v1/oauth/token")
        )
    }
}