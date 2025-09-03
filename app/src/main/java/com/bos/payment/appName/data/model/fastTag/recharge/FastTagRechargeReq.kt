package com.bos.payment.appName.data.model.fastTag.recharge

import com.google.gson.annotations.SerializedName


data class FastTagRechargeReq(

    @SerializedName("operator"       ) var operator: Int?       = null,
    @SerializedName("canumber"       ) var canumber: String?    = null,
    @SerializedName("amount"         ) var amount: String?       = null,
    @SerializedName("referenceid"    ) var referenceid: String?    = null,
    @SerializedName("latitude"       ) var latitude: String?    = null,
    @SerializedName("longitude"      ) var longitude: String?    = null,
    @SerializedName("bill_fetch"     ) var billFetch: BillFetch? = BillFetch(),
    @SerializedName("RegistrationID" ) var RegistrationID: String?    = null

)