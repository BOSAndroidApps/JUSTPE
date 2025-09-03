package com.bos.payment.appName.data.model.kyc

import com.google.gson.annotations.SerializedName


data class ReteriveAgentKYCRes (

  @SerializedName("RID"                    ) var RID                   : Int?    = null,
  @SerializedName("AgentType"              ) var AgentType             : String? = null,
  @SerializedName("AgencyName"             ) var AgencyName            : String? = null,
  @SerializedName("FirstName"              ) var FirstName             : String? = null,
  @SerializedName("EmailID"                ) var EmailID               : String? = null,
  @SerializedName("DOB"                    ) var DOB                   : String? = null,
  @SerializedName("AlternateMobileNo"      ) var AlternateMobileNo     : String? = null,
  @SerializedName("PermanentAddress"       ) var PermanentAddress      : String? = null,
  @SerializedName("State"                  ) var State                 : String? = null,
  @SerializedName("District"               ) var District              : String? = null,
  @SerializedName("AddharCardNo"           ) var AddharCardNo          : String? = null,
  @SerializedName("WebSite"                ) var WebSite               : String? = null,
  @SerializedName("RegistrationId"         ) var RegistrationId        : String? = null,
  @SerializedName("PanCardNumber"          ) var PanCardNumber         : String? = null,
  @SerializedName("MobileNo"               ) var MobileNo              : String? = null,
  @SerializedName("OfficeAddress"          ) var OfficeAddress         : String? = null,
  @SerializedName("City"                   ) var City                  : String? = null,
  @SerializedName("LastName"               ) var LastName              : String? = null,
  @SerializedName("Pincode"                ) var Pincode               : String? = null,
  @SerializedName("BusinessType"           ) var BusinessType          : String? = null,
  @SerializedName("GSTNO"                  ) var GSTNO                 : String? = null,
  @SerializedName("RefrenceID"             ) var RefrenceID            : String? = null,
  @SerializedName("RefrenceType"           ) var RefrenceType          : String? = null,
  @SerializedName("UploadPanCard"          ) var UploadPanCard         : String? = null,
  @SerializedName("UploadAddharCard_Front" ) var UploadAddharCardFront : String? = null,
  @SerializedName("UploadAddharCard_Back"  ) var UploadAddharCardBack  : String? = null,
  @SerializedName("UploadOtherProof"       ) var UploadOtherProof      : String? = null,
  @SerializedName("UploadPhoto"            ) var UploadPhoto           : String? = null,
  @SerializedName("AccountHolderName"      ) var AccountHolderName     : String? = null,
  @SerializedName("BankName"               ) var BankName              : String? = null,
  @SerializedName("BranchName"             ) var BranchName            : String? = null,
  @SerializedName("AccountNo"              ) var AccountNo             : String? = null,
  @SerializedName("AccountType"            ) var AccountType           : String? = null,
  @SerializedName("IFSCCode"               ) var IFSCCode              : String? = null,
  @SerializedName("Status"                 ) var Status                : Boolean? = null,
  @SerializedName("message"                ) var message               : String? = null,
  @SerializedName("Value"                  ) var Value                 : String? = null

)