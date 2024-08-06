package hu.ocist.enaplo.login.data.repository

import hu.ocist.enaplo.login.data.UserPreferences
import hu.ocist.enaplo.login.data.network.TeacherAuthApi
import hu.ocist.enaplo.login.data.requests.LoginRequest

class TeacherAuthRepository(
    private val api: TeacherAuthApi,
    private val preferences: UserPreferences
) : SafeApiCall {

    suspend fun login(
        loginRequest: LoginRequest
    ) = safeApiCall {
        api.login(loginRequest)
    }

    suspend fun saveJwtToken(token: String) {
        preferences.saveJwtToken(token)
    }

}