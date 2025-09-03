package com.bos.payment.appName.data.model.creditCard

import com.google.gson.annotations.SerializedName


data class CreditCardBillPaymentRes (

  @SerializedName("StatusCode"     ) var StatusCode     : String? = null,
  @SerializedName("Status"         ) var Status         : String? = null,
  @SerializedName("ExternalRef"    ) var ExternalRef    : String? = null,
  @SerializedName("TxnValue"       ) var TxnValue       : String? = null,
  @SerializedName("TxnReferenceId" ) var TxnReferenceId : String? = null,
  @SerializedName("CardNumber"     ) var CardNumber     : String? = null,
  @SerializedName("CardHolderName" ) var CardHolderName : String? = null,
  @SerializedName("CardNetwork"    ) var CardNetwork    : String? = null,
  @SerializedName("BankName"       ) var BankName       : String? = null,
  @SerializedName("Timestamp"      ) var Timestamp      : String? = null,
  @SerializedName("RegistrationID" ) var RegistrationID : String? = null,
  @SerializedName("Message"        ) var Message        : String? = null

)