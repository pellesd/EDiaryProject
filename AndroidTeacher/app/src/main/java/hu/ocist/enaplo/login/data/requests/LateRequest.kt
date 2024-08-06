package hu.ocist.enaplo.login.data.requests


import hu.ocist.enaplo.login.data.responses.LateResponse

data class LateRequest (
    val lessonId: Int,
    val dividendId: Int,
    val date: String,
    val lates: List<LateResponse>
)

