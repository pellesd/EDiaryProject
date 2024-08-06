@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.responses

data class MessageResponse(
    val id: kotlin.Int,
    val date: kotlin.String,
    val teacher: kotlin.String,
    val message: kotlin.String,
    val seen: Boolean
)