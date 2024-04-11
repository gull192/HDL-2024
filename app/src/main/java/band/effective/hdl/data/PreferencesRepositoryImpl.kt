package band.effective.hdl.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import band.effective.hdl.domain.PreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor (
    @ApplicationContext private val context: Context
) : PreferencesRepository {

    companion object {
        private val Context.appPreferences: DataStore<Preferences>
                by preferencesDataStore("leaderId_pref")
        
        val IS_AUTHORIZED = booleanPreferencesKey("IS_AUTHORIZED")
        val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
        val USER_ID = stringPreferencesKey("USER_ID")
    }



    override val accessToken: Flow<String?> = context.appPreferences.data.map { preferences ->
        preferences[ACCESS_TOKEN]
    }

    override val refreshToken: Flow<String?> =
        context.appPreferences.data.map { preferences ->
        preferences[REFRESH_TOKEN]
    }

    override val userId: Flow<String?> =
        context.appPreferences.data.map { preferences ->
            preferences[USER_ID]
        }

    override val isAuth: Flow<Boolean?> =
        context.appPreferences.data.map { preferences ->
            preferences[IS_AUTHORIZED]
        }

    override suspend fun changeAuthTokens(
        accessToken: String,
        refreshToken: String
    ) {
        context.appPreferences.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun changeAuthorizationState(state: Boolean) {
        context.appPreferences.edit { preferences ->
            preferences[IS_AUTHORIZED] = state
        }
    }

    override suspend fun changeUserId(userId: String) {
        context.appPreferences.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

}