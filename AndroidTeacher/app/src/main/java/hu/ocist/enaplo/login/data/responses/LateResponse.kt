@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.responses

data class LateResponse(
    val int: kotlin.Int,
    val string: kotlin.String,
    var len: kotlin.Int?
)