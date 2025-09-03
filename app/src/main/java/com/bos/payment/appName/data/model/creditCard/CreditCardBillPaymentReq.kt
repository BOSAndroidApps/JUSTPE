package com.bos.payment.appName.data.model.creditCard

import com.google.gson.annotations.SerializedName


data class CreditCardBillPaymentReq (

  @SerializedName("CardHolderName" ) var CardHolderName : String? = null,
  @SerializedName("cardNumber"     ) var cardNumber     : String? = null,
  @SerializedName("transferAmount" ) var transferAmount : String? = null,
  @SerializedName("externalRef"    ) var externalRef    : String? = null,
  @SerializedName("latitude"       ) var latitude       : String? = null,
  @SerializedName("longitude"      ) var longitude      : String? = null,
  @SerializedName("Remarks"        ) var Remarks        : String? = null,
  @SerializedName("alertEmail"     ) var alertEmail     : String? = null,
  @SerializedName("RegistrationID" ) var RegistrationID : String? = null

)