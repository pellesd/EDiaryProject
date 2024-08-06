package hu.ocist.enaplo.login.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.data.repository.StudentAuthRepository
import hu.ocist.enaplo.login.data.requests.LoginRequest
import kotlinx.coroutines.launch
import hu.ocist.enaplo.login.data.responses.StringResponse

class StudentAuthViewModel(
    private val repository: StudentAuthRepository
) : ViewModel() {

    private val _loginResponse : MutableLiveData<Resource<StringResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<StringResponse>>
        get() = _loginResponse

    fun login(
        loginRequest: LoginRequest
    ) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
       _loginResponse.value = repository.login(loginRequest)
    }

    suspend fun saveJwtToken(token: String) {
        repository.saveJwtToken(token)
    }
}