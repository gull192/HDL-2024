package band.effective.hdl.data.auth

import band.effective.hdl.domain.PreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

internal class AppAuthenticator @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val appAuth: AppAuth
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val result = runCatching {
                updateToken()
            }.getOrNull()
            return@runBlocking if (result != null) {
                response.request.addTokenHeader()
            } else {
                null
            }
        }
    }

    private suspend fun updateToken() {
        val refreshRequest = appAuth.getRefreshTokenRequest()
        appAuth.performTokenRequest(refreshRequest)
    }

    private fun Request.addTokenHeader(): Request {
        val authHeaderName = "Authorization"
        return newBuilder()
            .apply {
                runBlocking {
                    val token = preferencesRepository.accessToken.first()
                    if (token != null) {
                        header(authHeaderName, token.withBearer())
                    }
                }
            }
            .build()
    }

    private fun String.withBearer() = "Bearer $this"
}