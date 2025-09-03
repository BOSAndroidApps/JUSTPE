package com.bos.payment.appName.ui.view.travel.busactivity
import com.bos.payment.appName.data.model.travel.bus.busRequery.BusRequeryRes
import com.bos.payment.appName.data.model.travel.bus.busRequery.PaXDetails
import com.bos.payment.appName.data.model.travel.bus.busTicket.BusBookingListRes
import com.bos.payment.appName.data.model.travel.bus.busTicket.DataItem

class BusTicketConsListClass {

    companion object{
        val UpcomingTicketList : MutableList<DataItem> = mutableListOf()
        var startDate : String= ""
        var endDate : String  = ""
        var RequeryResponse: BusRequeryRes? = null
        val PendingTicketList : MutableList<com.bos.payment.appName.data.model.travel.bus.busRequery.PaXDetails> =    mutableListOf()
        val CancelledTicketList : MutableList<com.bos.payment.appName.data.model.travel.bus.busRequery.PaXDetails> =    mutableListOf()
        val CompletedTicketList : MutableList<com.bos.payment.appName.data.model.travel.bus.busRequery.PaXDetails> =    mutableListOf()
    }

}