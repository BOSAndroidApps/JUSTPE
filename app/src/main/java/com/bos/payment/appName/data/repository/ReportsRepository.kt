package com.bos.payment.appName.data.repository

import com.bos.payment.appName.data.model.reports.rechargeAndBill.RechargeWiseReportReq
import com.bos.payment.appName.network.ApiInterface

class ReportsRepository(private val apiInterface: ApiInterface) {

    suspend fun getAllRechargeAndBill(req: RechargeWiseReportReq) = apiInterface.rechargeBill(req)
}