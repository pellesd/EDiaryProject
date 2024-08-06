@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AbsenceResponse(
    val date: String,
    val day: String,
    val normalAuthorizedAbsence: Boolean,
    val notPending: Boolean,
    val numberOfLesson: Int,
    val schoolInterest: Boolean,
    val subject: String,
    val unauthorizedAbsence: Boolean
)