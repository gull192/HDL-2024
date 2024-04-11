package band.effective.hdl.data.auth

import band.effective.hdl.domain.PreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

internal class AuthorizationInterceptor @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request()
            .addTokenHeader()
            .let { chain.proceed(it) }
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