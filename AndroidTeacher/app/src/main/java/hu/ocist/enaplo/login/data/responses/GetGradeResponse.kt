@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.responses

data class GetGradeResponse(
    val studentId: Int,
    val student: String,
    var average: Float
)