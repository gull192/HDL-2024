package band.effective.hdl.domain

import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.TokenRequest

interface LeaderIdRepository {
    suspend fun auth(code: String): Result<Unit>
    suspend fun getRequestToken() : AuthorizationRequest

    suspend fun performeToken(tokenRequest: TokenRequest)
}