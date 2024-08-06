@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LateResponse(
    val minute: Int,
    val date: String,
    val day: String,
    val numberOfLesson: Int,
    val subject: String,
)