@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.requests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GradesRequest(
    val semester: kotlin.String,
    val subject: kotlin.String,
    val studentId: kotlin.Int,
)