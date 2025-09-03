package com.bos.payment.appName.data.model.travel.bus.busRequery

data class TicketDetailsForGenerateTicket(
    val cancelPolicy:String?,
    val pnrNumber:String,
    val orderId:String,
    val departureFrom:String,
    val arrivalTo:String,
    val departureTime:String,
    val departureDate:String,
    val arrivalTime:String,
    val arrivalDate:String,
    val busOperatorName:String,
    val driverContactVehicleNumber:String,
    val boardingPoint:String,
    val droppingPoint:String,
    val reportingTime:String,
    val boardingTime:String,
    val busType:String,
    val baseFare:String,
    val gst:String,
    val otherCharge:String,
    val convenienceFee:String,
    val totalAmount:String
)
