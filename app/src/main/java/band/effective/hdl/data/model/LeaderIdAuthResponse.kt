package band.effective.hdl.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LeaderIdAuthResponse(
    @Json(name = "access_token")
    val accessToken: String,
    @Json(name = "refresh_token")
    val refreshToken: String,
    @Json(name = "user_id")
    val userId: String,
    @Json(name = "user_validated")
    val userValidated: Boolean
)