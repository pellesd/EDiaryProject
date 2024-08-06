@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.requests


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostMessageTeacherRequest (
    val teacherid: kotlin.Int? = null,
    val message: kotlin.String? = null
)

