package band.effective.hdl.data

import android.util.Log
import band.effective.hdl.data.auth.AppAuth
import band.effective.hdl.data.model.LeaderIdAuthRequest
import band.effective.hdl.domain.LeaderIdRepository
import band.effective.hdl.domain.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.TokenRequest
import javax.inject.Inject

class LeaderIdRepositoryImpl @Inject constructor(
    private val preferences: PreferencesRepository,
    private val leaderIdApi: LeaderIdApi,
    private val appAuth: AppAuth
) : LeaderIdRepository {
    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    override suspend fun auth(code: String): Result<Unit> {
        val request = LeaderIdAuthRequest.getAuthByCodeRequest(code)
        val response = leaderIdApi.auth(request)
        if (response.isSuccess) {
            val data = response.getOrThrow()
                Log.d("auth", "ok")
                preferences.changeAuthTokens(
                    accessToken = data.accessToken,
                    refreshToken = data.refreshToken
                )
                preferences.changeUserId(data.userId)
                preferences.changeAuthorizationState(true)
        }
        return response.map {  }
    }

    override suspend fun getRequestToken(): AuthorizationRequest =
        appAuth.getAuthRequest()

    override suspend fun performeToken(tokenRequest: TokenRequest) {
        appAuth.performTokenRequest(tokenRequest)
    }
}