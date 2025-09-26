package com.bos.payment.appName.utils

import android.graphics.Bitmap
import android.widget.ArrayAdapter
import com.bos.payment.appName.data.model.recharge.operator.Data
import com.google.gson.internal.bind.ArrayTypeAdapter

object Constants {

    var agentTypeAdapter: ArrayTypeAdapter<String>? = null

    val FileName="notes/aopaytravel.txt"

    var RegistrationId = "RegistrationId"
    var MerchantId = "MerchantId"
    var MerchantList = "MerchantList"
    var Merchant = "Merchant"
    var imageUrl = "https://letsenhance.io/static/73136da51c245e80edc6ccfe44888a99/1015f/MainBefore.jpg"
    var operatorImage = "operatorImage"
    var IS_LOGIN = "IS_LOGIN"
    var loginId = "loginId"
    var loginPassword = "loginPassword"
    var AgentName = "AgentName"
    var AgentType = "AgentType"
    var WalletBalance = "WalletBalance"
    var ActiveStatus = "ActiveStatus"
    var Status = "Status"
    var Password = "Password"
//    var CompanyCode = "cmp1045"
    var ReferenceId = "ReferenceId"
    var ReferenceType = "ReferenceType"
    var PanCardNo = "PanCardNo"
    var FirstName = "FirstName"
    var LastName = "LastName"
    var DOB = "DOB"
    var IS_FIRST_LAUNCH = true
    var fingerPrintAction = false
    var AlternateMobileNumber = "AlternateMobileNumber"
    var PermanentAddress = "PermanentAddress"
    var BusinessType = "BusinessType"
    var OfficeAddress = "OfficeAddress"
    var State = "State"
    var District = "District"
    var City = "City"
    var PinCode = "PinCode"
    var stateRespDMT = "stateRespDMT"
    var PinCodeDMT = "PinCodeDMT"
    var dobOwnerDMT = "dobOwnerDMT"
    var addressOwnerDMT = "addressOwnerDMT"
    var AadharCardNo = "AadharCardNo"
    var GSTNo = "GSTNo"
    var Website = "Website"
    var AccountHolderName = "AccountHolderName"
    var AccountNumber = "AccountNumber"
    var BankName = "BankName"
    var BranchName = "BranchName"
    var AccountType = "AccountType"
    var IFSCCODE = "IFSCCODE"
    var PanCardPic = "PanCardPic"
    var AadharFront = "AadharFront"
    var AadharBack = "AadharBack"
    var Photo = "Photo"
    var EncryptionKey = "EncryptionKey"
    var billAmount = "billAmount"
    var billnetamount = "billnetamount"
    var dueDate = "dueDate"
    var maxBillAmount = "maxBillAmount"
    var cellNumber = "cellNumber"
    var userName = "userName"
    var jio = "jio"
    var bsnl = "bsnl"
    var idea = "idea"
    var airtel = "airtel"
    var vodafone = "vodafone"
    var noImage = "noImage"
    var beneficiaryId = "beneficiaryId"
    var CompanyCode = "companyCode"
    var CompanyLogo = "CompanyLogo"
    var CompanyName = "CompanyName"
    var MobileNumber = "MobileNumber"
    var EmailID = "EmailID"
    var BillAPI = "F0116"
    var FatTagAPI = "F0118"
    var PayoutAPI = "F0112"
    var mobileRechareAPI = "F0117"
    var clientStateResponse = "clientStateResponse"
    var acceptPayment: Boolean = false
    var acceptPartPay: Boolean = false
    var isUpdate:Boolean = false
    var RechargeAPI_Status = "RechargeAPI_Status"
    var RechargeAPI_2_Status = "RechargeAPI_2_Status"
    var MoneyTransferAPI_Status = "MoneyTransferAPI_Status"
    var MoneyTransferAPI_2_Status = "MoneyTransferAPI_2_Status"
    var Payout_API_2_Status = "Payout_API_2_Status"
    var Payout_API_Status = "Payout_API_Status"
    var Payin_API_Status = "Payin_API_Status"
    var Payin_API_2_Status = "Payin_API_2_Status"
    var Fastag_API_Status = "Fastag_API_Status"
    var PANCardAPI_Status = "PANCardAPI_Status"
    var CreditCardAPI_Status = "CreditCardAPI_Status"
    var AEPS_API_Status = "AEPS_API_Status"
    var APIName = "APIName"
    var AllAPIName = "AllAPIName"
    var OperatorCategory = "OperatorCategory"
    var opCategory = "opCategory"
    var circleName = "circleName"
    val OperatorId = 0
    var rechargeAmount = "rechargeAmount"
    var beneId = "beneId"
    var bankAccount = "bankAccount"
    var ifscCode = "ifscCode"
    var bankOwnerName = "bankOwnerName"
    var mobileOperatorName = "mobileOperatorName"
    var mobileCircleName = "mobileCircleName"
    var uploadImage = ""
    var retailerCommissionWithoutTDS = "retailerCommissionWithoutTDS"
    var adminCommissionWithoutTDS = "adminCommissionWithoutTDS"
    var mDistributerCommissionWithoutTDS = "mDistributerCommissionWithoutTDS"
    var customerCommissionWithoutTDS = "customerCommissionWithoutTDS"
    var tds = "tds"
    var totalTransaction = "totalTransaction"
    var serviceCharge = "serviceCharge"
    var serviceChargeWithGST = "serviceChargeWithGST"
    var actualAmountServiceChargeWithGST = "actualAmountServiceChargeWithGST"
    var finalCommission = "finalCommission"
    var finalSubDisCommission = "finalSubDisCommission"
    var finalMDisCommission = "finalMDisCommission"
    var imagePath = ""
    var deviceIPAddress = "deviceIPAddress"
    var mDis_CommissionType = "mDis_CommissionType"
    var admin_CommissionType = "admin_CommissionType"
    var retailer_CommissionType = "retailer_CommissionType"
    var customer_CommissionType = "customer_CommissionType"
    var serviceType = "serviceType"
    var dIS_RegistrationId = "dIS_RegistrationId"
    var mD_RegistrationId = "mD_RegistrationId"
    var retailerMainVirtualAmount = "retailerMainVirtualAmount"
    var retailerCreditLimit = "retailerCreditLimit"
    var retailerAvailCreditLimit = "retailerCreditLimit"
    var retailerHoldAmt = "retailerHoldAmt"
    var retailerActualAvailAmount = "retailerActualAvailAmount"
    var availCreditAmount = "availCreditAmount"
    var merchantBalance = "merchantBalance"

    //View Bill
    var posPaidBillAmount = "posPaidBillAmount"
    var posPaidBillNetAmount = "posPaidBillNetAmount"
    var posPaidDueDate = "posPaidDueDate"
    var posPaidBillDate = "posPaidBillDate"
    var posPaidCellNumber = "posPaidCellNumber"
    var posPaidUserName = "posPaidUserName"
    var fromDesignationName = "fromDesignationName"
    var fromDesignationId = "fromDesignationId"
    var toDesignationName = "toDesignationName"
    var toDesignationId = "toDesignationId"
    var dateAndTime = "dateAndTime"
    var searchKey = "searchKey"
    var busKey = "busKey"
    var boarding_Id = "boarding_Id"
    var dropping_Id = "dropping_Id"
    var travelCompanyName = "travelCompanyName"
    var busName = "busName"
    var travelAmount = "travelAmount"
    var arrivalTime = "arrivalTime"
    var travelTime = "travelTime"
    var seatMap_Key = "seatMap_Key"
    var seatNumber = "seatNumber"
    var booking_RefNo = "booking_RefNo"
    var requestId = "requestId"
    var boardingPoint = "boardingPoint"
    var droppingPoint = "droppingPoint"
    var FlightSearchKey = "search_Key"
    var FlightKey = "flight_key"
    var AirTotalTicketPrice = "flight_ticket_price"
    var BookingRefNo = "bookingRefNo"

    val KEY_192 = "your-24-byte-key-here!".toByteArray(Charsets.UTF_8) // 24 bytes
    val IV_192 = "8-byte-iv".toByteArray(Charsets.UTF_8) // 8 bytes

    var posPaidAcceptPayment: Boolean = false
    var posPaidAcceptPartPay: Boolean = false

    var TYPE = "RTE"

    var compressedBitmap: Bitmap? = null
    const val OWNER_PAN_CARD: Int = 1
    const val OWNER_AADHAR_FRONT: Int = 2
    const val OWNER_AADHAR_BACK: Int = 3
    const val OWNER_PHOTO: Int = 4
    const val OTHER_PHOTO: Int = 5
    const val UPLOAD_PHOTO: Int = 6
    const val UPLOAD_SLIP: Int = 7


    var dropDownValues: ArrayList<Data>? = null
    var genderAdapter: ArrayAdapter<String>? = null
    var state_adapter: ArrayAdapter<String>? = null
    var getAllOperatorAdapter: ArrayAdapter<String>? = null
    var getAllCircleAdapter: ArrayAdapter<String>? = null
    var getAllOperatorAdapterValue: ArrayAdapter<String>? = null
    var getAllGasOperatorAdapter: ArrayAdapter<String>? = null
    var getAllDTHOperatorAdapter: ArrayAdapter<String>? = null
    var getAllInsuranceOperatorAdapter: ArrayAdapter<String>? = null
    var getAllBankListAdapter: ArrayAdapter<String>? = null
    var getAllBusListAdapter: ArrayAdapter<String>? = null
    var getAllBoardingPointAdapter: ArrayAdapter<String>? = null
    var getAllDroppingPointAdapter: ArrayAdapter<String>? = null


    var operatorName: ArrayList<String>? = null
    var operatorNameDth: ArrayList<String>? = null
    var productIdList: ArrayList<String>? = null
    var dthName: ArrayList<String>? = null
    var emiNo: ArrayList<String>? = null
    var gasName: ArrayList<String>? = null
    var insuranceName: ArrayList<String>? = null
    var broadBandName: ArrayList<String>? = null
    var electricityName: ArrayList<String>? = null
    var waterName: ArrayList<String>? = null
    var prepaidName: ArrayList<String>? = null
    var landLineName: ArrayList<String>? = null
    var municipalityName: ArrayList<String>? = null
    var fastTagName: ArrayList<String>? = null
    var stateName: ArrayList<String>? = null
//    var merchantIdList: ArrayList<String>? = null
    var bankListName: ArrayList<String>? = null
    var bankListId: ArrayList<String>? = null
    var busListName: ArrayList<String>? = null
    var toLocationName: ArrayList<String>? = null
    var boardingPointName: ArrayList<String>? = null
    var droppingPointName: ArrayList<String>? = null
    var titleName: ArrayList<String>? = null
    var genderName: ArrayList<String>? = null

    var merchantIdList: MutableList<String>? = null


    var stateNameMap: HashMap<String, Int>? = null
    var operatorNameMap: HashMap<String, Int>? = null
    var dthNameMap: HashMap<String, Int>? = null
    var emiNoMap: HashMap<String, Int>? = null
    var gasNameMap: HashMap<String, Int>? = null
    var insuranceNameMap: HashMap<String, Int>? = null
    var broadBandNameMap: HashMap<String, Int>? = null
    var electricityNameMap: HashMap<String, Int>? = null
    var waterNameMap: HashMap<String, Int>? = null
    var prepaidNameMap: HashMap<String, Int>? = null
    var landLineNameMap: HashMap<String, Int>? = null
    var municipalityNameMap: HashMap<String, Int>? = null
    var fastTagNameMap: HashMap<String, Int>? = null
    var bankListNameMap: HashMap<String, Int>? = null
    var busListNameMap: HashMap<String, Int>? = null
    var toLocationNameMap: HashMap<String, Int>? = null
//    var beneficiaryIdMap: HashMap<String, Int>? = null


    var operatorNameMapForGettingOperatorName: HashMap<Int, String>? = null
    var dthNameMapForGettingDthName: HashMap<Int, String>? = null
    var emiNoMapForGettingEmiNo: HashMap<Int, String>? = null
    var gasNameMapForGettingGasName: HashMap<Int, String>? = null
    var insuranceNameMapForGettingInsuranceName: HashMap<Int, String>? = null
    var broadBandNameMapForGettingBroadBandName: HashMap<Int, String>? = null
    var electricityNameMapForGettingElectricityName: HashMap<Int, String>? = null
    var waterNameMapForGettingWaterName: HashMap<Int, String>? = null
    var prepaidNameMapForGettingPrepaidName: HashMap<Int, String>? = null
    var landLineNameMapForGettingLandLineName: HashMap<Int, String>? = null
    var municipalityNameMapForGettingMunicipalityName: HashMap<Int, String>? = null
    var fastTagNameMapForGettingFastTagName: HashMap<Int, String>? = null
    var bankListNameMapForGettingbankListName: HashMap<Int, String>? = null
    var busListNameMapForGettingBusListName: HashMap<Int, String>? = null
    var toLocationNameMapForGettingToLocationName: HashMap<Int, String>? = null






}
