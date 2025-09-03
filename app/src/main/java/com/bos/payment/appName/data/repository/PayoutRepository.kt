package com.bos.payment.appName.data.repository

import com.bos.payment.appName.data.model.idfcPayout.AOPPayOutReq
import com.bos.payment.appName.data.model.recharge.payout.PayoutAmountReq
import com.bos.payment.appName.data.model.recharge.payout.payoutBos.PayoutAppReq
import com.bos.payment.appName.data.model.recharge.payout.payoutStatus.PayoutStatusReq
import com.bos.payment.appName.network.ApiInterface

class PayoutRepository(private val apiInterface: ApiInterface) {
    suspend fun sendAmount(req: PayoutAmountReq) = apiInterface.sendAmount(req)
    suspend fun getPayoutStatus(req: PayoutStatusReq) = apiInterface.getPayoutStatus(req)
    suspend fun getPayoutAmountTransaction(req: PayoutAppReq) = apiInterface.getPayoutAmountTransaction(req)
    suspend fun sendAllPayoutAmount(req: AOPPayOutReq) = apiInterface.sendAllPayoutAmount(req)

}