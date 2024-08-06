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
data class AddGradeRequest (
    val dividendId: kotlin.Int,
    val grade: kotlin.Int,
    val gradeString: kotlin.String,
    val studentId: kotlin.Int,
    val date: kotlin.String,
    val text: kotlin.String? = null,
    val multiplier: kotlin.Int,
)

