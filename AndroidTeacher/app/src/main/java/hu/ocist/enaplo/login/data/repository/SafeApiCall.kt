package hu.ocist.enaplo.login.data.repository

import hu.ocist.enaplo.login.data.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException

interface SafeApiCall {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ) : Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable){
                when(throwable) {
                    is HttpException -> {
                        Resource.Failure(false, throwable.code(), throwable.response()?.errorBody())
                    }
                    is SocketTimeoutException -> {
                        Resource.Failure(false, 504, null)
                    }
                    else -> {
                        println("Hiba: ")
                        println(throwable.message)
                        Resource.Failure(true, null, null)
                    }
                }
            }
        }
    }
}