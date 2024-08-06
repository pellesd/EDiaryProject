@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.requests

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CanteenRequest (
    val id: Int?,
    val date: String,
    val firstMeal: String,
    val secondMeal: String,
    val extra: String?
)
