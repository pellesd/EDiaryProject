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
    val id: kotlin.Int,
    val date: kotlin.String,
    var firstMeal: kotlin.String,
    var secondMeal: kotlin.String,
    var extra: kotlin.String?
)
