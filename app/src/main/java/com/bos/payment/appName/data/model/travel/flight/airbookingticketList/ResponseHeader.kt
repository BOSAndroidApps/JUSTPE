package com.bos.payment.appName.data.model.travel.flight.airbookingticketList

import com.google.gson.annotations.SerializedName

data class ResponseHeader(@SerializedName("status_Id")
                          val statusId: String = "",
                          @SerializedName("error_Code")
                          val errorCode: String = "",
                          @SerializedName("error_Desc")
                          val errorDesc: String = "",
                          @SerializedName("error_InnerException")
                          val errorInnerException: String = "",
                          @SerializedName("request_Id")
                          val requestId: String = "")