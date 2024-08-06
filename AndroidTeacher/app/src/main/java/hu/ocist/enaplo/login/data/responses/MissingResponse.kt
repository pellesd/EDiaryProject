@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.responses

data class MissingResponse(
    val int: kotlin.Int,
    val string: kotlin.String,
    var bool: kotlin.Boolean
)