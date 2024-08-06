@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.requests


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IntStringRequest (
    val int: kotlin.Int? = null,
    val string: kotlin.String? = null
)

