@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.requests


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Login api hívás bemenő típusa.
 *
 * @param username 
 * @param password 
 */
@JsonClass(generateAdapter = true)
data class LoginRequest (
    val username: kotlin.String? = null,
    val password: kotlin.String? = null
)

