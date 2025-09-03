package com.bos.payment.appName.data.model.merchant.apiServiceCharge

import com.google.gson.annotations.SerializedName

data class Data (

  @SerializedName("admin_CommissionType"    ) var adminCommissionType    : String? = null,
  @SerializedName("admin_Commission"        ) var adminCommission        : String? = null,
  @SerializedName("retailer_CommissionType" ) var retailerCommissionType : String? = null,
  @SerializedName("retailer_Commission"     ) var retailerCommission     : String? = null,
  @SerializedName("customerCommissionType"  ) var customerCommissionType : String? = null,
  @SerializedName("customerCommission"      ) var customerCommission     : String? = null,
  @SerializedName("serviceType"             ) var serviceType            : String? = null,
  @SerializedName("serviceCharge"           ) var serviceCharge          : String? = null,
  @SerializedName("modeOfPayment"           ) var modeOfPayment          : String? = null,
  @SerializedName("typeofCrad"              ) var typeofCrad             : String? = null,
  @SerializedName("slabName"                ) var slabName               : String? = null

)