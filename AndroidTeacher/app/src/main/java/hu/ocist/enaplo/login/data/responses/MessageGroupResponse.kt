@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.responses

data class MessageGroupResponse(
    val id: kotlin.Int,
    val date: kotlin.String,
    val message: kotlin.String,
    val validUntil: kotlin.String
)