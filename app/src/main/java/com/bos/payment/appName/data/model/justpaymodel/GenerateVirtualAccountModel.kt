package com.bos.payment.appName.data.model.justpaymodel

import com.google.gson.annotations.SerializedName

data class GenerateVirtualAccountModel(
    @SerializedName("apiId")
    var apiId : String,
    @SerializedName("bank_id")
    var bankid : String,
    @SerializedName("partnerReferenceNo")
    var partnerReferenceNo : String,
    @SerializedName("p1_businessName")
    var p1businessName : String ? ,
    @SerializedName("p2_settlementAccountName")
    var p2settlementAccountName : String ?,
    @SerializedName("p3_sellerIdentifier")
    var p3sellerIdentifier : String?,
    @SerializedName("p4_mobileNumber")
    var p4mobileNumber : String ?,
    @SerializedName("p5_emailId")
    var p5emailId : String ?,
    @SerializedName("p6_mcc")
    var p6mcc : String,
    @SerializedName("p7_turnoverType")
    var p7turnoverType : String,
    @SerializedName("p8_acceptanceType")
    var p8acceptanceType : String,
    @SerializedName("p9_ownershipType")
    var p9ownershipType : String,
    @SerializedName("p10_city")
    var p10city : String,
    @SerializedName("p11_district")
    var p11district : String,
    @SerializedName("p12_stateCode")
    var p12stateCode : String,
    @SerializedName("p13_pincode")
    var p13pincode : String,
    @SerializedName("p14_pan")
    var p14pan : String,
    @SerializedName("p15_gstNumber")
    var p15gstNumber : String,
    @SerializedName("p16_settlementAccountNumber")
    var p16settlementAccountNumber : String ?,
    @SerializedName("p17_settlementAccountIfsc")
    var p17settlementAccountIfsc : String ?,
    @SerializedName("p18_Latitude")
    var p18Latitude : String,
    @SerializedName("p19_Longitude")
    var p19Longitude : String,
    @SerializedName("p20_addressLine1")
    var p20addressLine1 : String,
    @SerializedName("p21_addressLine2")
    var p21addressLine2 : String,
    @SerializedName("p22_LLPIN_CIN")
    var p22LLPINCIN : String,
    @SerializedName("p26_DOB")
    var p26DOB : String,
    @SerializedName("p27_dOI")
    var p27dOI : String,
    @SerializedName("p28_websiteURL_AppPackageName")
    var p28websiteURLAppPackageName : String,
    @SerializedName("RegistrationID")
    var RegistrationID : String ?,

)
