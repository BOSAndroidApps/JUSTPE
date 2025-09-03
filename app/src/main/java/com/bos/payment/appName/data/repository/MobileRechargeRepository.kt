package com.bos.payment.appName.data.repository

import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.MobileWiseRechargeReq
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.RechargeCategoryReq
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.RechargeOperatorsReq
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.RechargePlanReq
import com.bos.payment.appName.data.model.recharge.recharge.DthInfoReq
import com.bos.payment.appName.data.model.recharge.recharge.MobileRechargeReq
import com.bos.payment.appName.data.model.travel.flight.FlightRequeryReq
import com.bos.payment.appName.data.model.travel.flight.GetAirTicketListReq
import com.bos.payment.appName.network.ApiInterface
import okhttp3.RequestBody

class MobileRechargeRepository(private var apiInterface: ApiInterface) {

    suspend fun getRechargeCategoryRequest(req: RechargeCategoryReq)= apiInterface.getRechargeCategory(req)

    suspend fun getRechargeOperatorsNameReq(req: RechargeOperatorsReq)= apiInterface.getRechargeOperatorNameReq(req)

    suspend fun getMobileWiseRechargeReq(req: MobileWiseRechargeReq)= apiInterface.getMobileWiseRechargeReq(req)

    suspend fun getDthInfoReq(req:DthInfoReq)= apiInterface.getDthInfoPlanReq(req)

    suspend fun getMobileRechargeReq(req: com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.MobileRechargeReq)= apiInterface.getMobileRechargeReq(req)


}