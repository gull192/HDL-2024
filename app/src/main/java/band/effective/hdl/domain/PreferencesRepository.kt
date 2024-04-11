package band.effective.hdl.domain

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    suspend fun changeAuthTokens(accessToken: String, refreshToken: String)

    suspend fun changeAuthorizationState(state: Boolean)

    suspend fun changeUserId(userId: String)


    val accessToken: Flow<String?>

    val refreshToken: Flow<String?>

    val userId: Flow<String?>

    val isAuth: Flow<Boolean?>
}