package band.effective.hdl.data.model

import band.effective.hdl.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LeaderIdAuthRequest(
    @Json(name = "client_id")
    val clientId: String,
    @Json(name = "client_secret")
    val clientSecret: String,
    @Json(name = "grant_type")
    val grantType: String,
    val code: String
) {
    companion object{
        fun getAuthByCodeRequest(code:String) = LeaderIdAuthRequest(
            clientId = BuildConfig.AUTH_CLIENT_ID,
            clientSecret = BuildConfig.AUTH_CLIENT_SECRET,
            grantType = "authorization_code",
            code = code
        )
    }
}