package com.example.example

import com.google.gson.annotations.SerializedName


data class LoginReq (

  @SerializedName("userID"             ) var userID      : String? = null,
  @SerializedName("agentPassword"      ) var agentPassword      : String? = null

)