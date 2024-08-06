package hu.ocist.enaplo.login.data.network

import retrofit2.http.*
import hu.ocist.enaplo.login.data.requests.LoginRequest
import hu.ocist.enaplo.login.data.responses.StringResponse

interface StudentAuthApi {
    @POST("studentauth/login")
    suspend fun studentAuthLoginPost(
        @Body loginRequest: LoginRequest
    ): StringResponse
}