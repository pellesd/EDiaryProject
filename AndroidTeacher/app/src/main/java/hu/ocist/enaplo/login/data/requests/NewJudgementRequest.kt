@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.requests


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewJudgementRequest (
    val studentId: kotlin.Int? = null,
    val text: kotlin.String? = null,
    val levelId: kotlin.Int? = null
)

