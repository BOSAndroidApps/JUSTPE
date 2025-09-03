package com.bos.payment.appName.data.model.recharge.mobile

data class Plan(
    val rs: Int,
    val validity: String,
    val desc: String
)

data class MobileRechargePlanModel(
    val arrayName: String,
    val plans: List<Plan>
)
