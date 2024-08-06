@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GradesResponse(
    val grade: Int,
    val gradeString: String,
    val teacher: String,
    val date: String,
    val text: String,
    val closed: Boolean,
)
