@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TimetableResponse(
    val id: Int,
    val dividendId: Int,
    val lesson: String,
    val numberOfLesson: Int,
    val teacher: String,
    val group: String,
    val room: String?,
)
