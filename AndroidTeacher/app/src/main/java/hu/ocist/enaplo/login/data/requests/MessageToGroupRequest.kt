@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.requests


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessageToGroupRequest (
    val groupid: kotlin.Int? = null,
    val text: kotlin.String? = null,
    val valid: kotlin.String? = null
)

