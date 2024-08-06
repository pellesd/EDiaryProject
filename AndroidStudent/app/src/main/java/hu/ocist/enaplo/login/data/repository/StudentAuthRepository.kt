package hu.ocist.enaplo.login.data.repository

import hu.ocist.enaplo.login.data.UserPreferences
import hu.ocist.enaplo.login.data.network.StudentAuthApi
import hu.ocist.enaplo.login.data.requests.LoginRequest

class StudentAuthRepository(
    private val api: StudentAuthApi,
    private val preferences: UserPreferences
) : SafeApiCall {

    suspend fun login(
        loginRequest: LoginRequest
    ) = safeApiCall {
        api.studentAuthLoginPost(loginRequest)
    }

    suspend fun saveJwtToken(token: String) {
        preferences.saveJwtToken(token)
    }

}