@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.responses

data class MessageTeacherResponse(
    val date: kotlin.String,
    val from: kotlin.String,
    val to: kotlin.String,
    val message: kotlin.String
)