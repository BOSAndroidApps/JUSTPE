package com.bos.payment.appName.data.model.loginSignUp.signUp

import com.bos.payment.appName.data.model.loginSignUp.Data
import com.google.gson.annotations.SerializedName


data class SignUpRes (

  @SerializedName("isSuccess"     ) var isSuccess     : Boolean?        = null,
  @SerializedName("returnMessage" ) var returnMessage : String?         = null,
  @SerializedName("returnCode"    ) var returnCode    : String?         = null,
  @SerializedName("data"          ) var data          : ArrayList<Data> = arrayListOf()


)