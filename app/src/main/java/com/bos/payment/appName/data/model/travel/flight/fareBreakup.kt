package com.bos.payment.appName.data.model.travel.flight

data class fareBreakup(
    var paxType: String= "",
    var airportTax_amount : String= "",
    var basic_amount: String= "",
    var quantity : Int= 0
)
