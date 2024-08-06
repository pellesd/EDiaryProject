@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CanteenResponse (
    //val date: kotlin.String, // I do not need the date, I know It
    val firstMeal: kotlin.String,
    val secondMeal: kotlin.String,
    val extra: kotlin.String?
)
