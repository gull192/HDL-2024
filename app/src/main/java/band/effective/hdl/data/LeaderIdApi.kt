package band.effective.hdl.data

import band.effective.hdl.data.model.LeaderIdAuthRequest
import band.effective.hdl.data.model.LeaderIdAuthResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LeaderIdApi {

    @POST("oauth/token")
    suspend fun auth(@Body leaderIdAuthResponse: LeaderIdAuthRequest): Result<LeaderIdAuthResponse>
}