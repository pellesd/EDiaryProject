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

    @Json(name = "username")
    val username: kotlin.String? = null,

    @Json(name = "password")
    val password: kotlin.String? = null
)

