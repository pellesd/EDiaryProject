package hu.ocist.enaplo.login.data.network

import retrofit2.http.*
import hu.ocist.enaplo.login.data.requests.LoginRequest
import hu.ocist.enaplo.login.data.responses.StringResponse

interface TeacherAuthApi {
    @POST("teacherauth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): StringResponse
}