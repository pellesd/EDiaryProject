@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.responses

import com.squareup.moshi.Json

data class MessageResponse(
    val date: kotlin.String,
    val sender: kotlin.String,
    val message: kotlin.String
)