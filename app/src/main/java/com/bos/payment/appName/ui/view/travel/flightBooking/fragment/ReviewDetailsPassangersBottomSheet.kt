package com.bos.payment.appName.ui.view.travel.flightBooking.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.travel.flight.AirRepriceRequests
import com.bos.payment.appName.data.model.travel.flight.AirReprintReq
import com.bos.payment.appName.data.model.travel.flight.AirTicketingReq
import com.bos.payment.appName.data.model.travel.flight.BookingFlightDetails
import com.bos.payment.appName.data.model.travel.flight.BookingSSRDetails
import com.bos.payment.appName.data.model.travel.flight.FlightAddPaymentReq
import com.bos.payment.appName.data.model.travel.flight.FlightRequeryReq
import com.bos.payment.appName.data.model.travel.flight.FlightTempBookingReq
import com.bos.payment.appName.data.model.travel.flight.FlightsItem
import com.bos.payment.appName.data.model.travel.flight.PaXDetailsFlight
import com.bos.payment.appName.data.model.travel.flight.SegmentsItem
import com.bos.payment.appName.data.model.walletBalance.merchantBal.GetMerchantBalanceReq
import com.bos.payment.appName.data.model.walletBalance.merchantBal.GetMerchantBalanceRes
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.GetBalanceReq
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.GetBalanceRes
import com.bos.payment.appName.data.repository.GetAllAPIServiceRepository
import com.bos.payment.appName.data.repository.TravelRepository
import com.bos.payment.appName.data.viewModelFactory.GetAllApiServiceViewModelFactory
import com.bos.payment.appName.data.viewModelFactory.TravelViewModelFactory
import com.bos.payment.appName.databinding.AddtravellersitemlayoutBinding
import com.bos.payment.appName.databinding.ContactmobileItemlayoutBinding
import com.bos.payment.appName.databinding.FlightdetailsItemBottomsheetBinding
import com.bos.payment.appName.databinding.GstDetailsLayoutBinding
import com.bos.payment.appName.databinding.ReviewDetailsPassangerItemlayoutBinding
import com.bos.payment.appName.databinding.TravellersclassItemBottomsheetBinding
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.view.travel.adapter.FlightTicketDetailsAdapter
import com.bos.payment.appName.ui.view.travel.adapter.PassangerDataList
import com.bos.payment.appName.ui.view.travel.adapter.PassangerDetailsListShownAdapter
import com.bos.payment.appName.ui.view.travel.adapter.TempBookingPassangerDetails
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.FlightDetails
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.adultCount
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.calculateTotalFlightDuration
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.childCount
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.className
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.infantCount
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.totalCount
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.totalDurationTime
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.AddDetailsPassangerActivity
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.AddDetailsPassangerActivity.Companion.adultList
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.AddDetailsPassangerActivity.Companion.childList
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.AddDetailsPassangerActivity.Companion.fareList
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.AddDetailsPassangerActivity.Companion.flightDetailsPassangerDetail
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.AddDetailsPassangerActivity.Companion.infantList
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.AddDetailsPassangerActivity.Companion.paxDetailsListFromReprice
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.AddDetailsPassangerActivity.Companion.segmentListPassangerDetail
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.FlightBookedTicketActivity
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.FlightBookedTicketActivity.Companion.BookedTicketList
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.FlightMainActivity
import com.bos.payment.appName.ui.viewmodel.GetAllApiServiceViewModel
import com.bos.payment.appName.ui.viewmodel.TravelViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.Constants.scanForActivity
import com.bos.payment.appName.utils.MStash
import com.bos.payment.appName.utils.Utils
import com.bos.payment.appName.utils.Utils.generateRandomNumber
import com.bos.payment.appName.utils.Utils.runIfConnected
import com.bos.payment.appName.utils.Utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

class ReviewDetailsPassangersBottomSheet:BottomSheetDialogFragment() {
    private lateinit var binding : ReviewDetailsPassangerItemlayoutBinding

    lateinit var viewModel : TravelViewModel
    private var mStash: MStash? = null
    lateinit var pd : ProgressDialog
    private lateinit var getAllApiServiceViewModel: GetAllApiServiceViewModel

    companion object {
        const val TAG = "ReviewDetailsBottomSheet"
        var passangerDetailsList: MutableList<PassangerDataList> = mutableListOf()
        var tempBookingPassangerDetails: MutableList<TempBookingPassangerDetails> = mutableListOf()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ReviewDetailsPassangerItemlayoutBinding.inflate(inflater, container, false)


        mStash = MStash.getInstance(requireContext())
        viewModel = ViewModelProvider(this, TravelViewModelFactory(TravelRepository(RetrofitClient.apiAllTravelAPI, RetrofitClient.apiBusAddRequestlAPI)))[TravelViewModel::class.java]
        getAllApiServiceViewModel = ViewModelProvider(this, GetAllApiServiceViewModelFactory(GetAllAPIServiceRepository(RetrofitClient.apiAllInterface)))[GetAllApiServiceViewModel::class.java]

        pd = ProgressDialog(requireContext())
        pd.setCanceledOnTouchOutside(false)

        setonclicklistner()
        setdataonview()
        return binding.root
    }


    fun setdataonview(){
        var adapter = PassangerDetailsListShownAdapter(requireContext(), passangerDetailsList)
        binding.showingpassangerlist.adapter= adapter
        adapter.notifyDataSetChanged()
    }



    private fun setonclicklistner(){

        binding.edittxt.setOnClickListener {
            dialog!!.dismiss()
        }

        binding.confrmtxt.setOnClickListener {
            hitApiForFlightTempBooking()
        }

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog?.setOnShowListener { it ->
            val d = it as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                    sheet ->
                val behavior = BottomSheetBehavior.from(sheet)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED

                val layoutParams = sheet.layoutParams
                val windowHeight = Resources.getSystem().displayMetrics.heightPixels
                layoutParams.height = windowHeight
                sheet.layoutParams = layoutParams
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }


    fun hitApiForFlightTempBooking(){
        var customMob =  mStash!!.getStringValue(Constants.MobileNumber, "")!!
        var passangermob = tempBookingPassangerDetails[0].passengerMobile
        var passemail = tempBookingPassangerDetails[0].passengerEmail
        var passgst = tempBookingPassangerDetails[0].gst
        var passgstnumber = tempBookingPassangerDetails[0].gsT_Number
        var passgstholdername = tempBookingPassangerDetails[0].gsTHolderName
        var passgstaddress = tempBookingPassangerDetails[0].gsTAddress

        val random10DigitNumber = (1000000000L..9999999999L).random() //random10DigitNumber.toString()
        Log.d("randomphone",random10DigitNumber.toString())

        var flightTempBookingreq = FlightTempBookingReq(
            customerMobile = customMob/*random10DigitNumber.toString()*/ , // for testing purpose
            passengerMobile =passangermob /*random10DigitNumber.toString()*/,
            whatsAPPMobile = passangermob /*random10DigitNumber.toString()*/,
            passengerEmail = passemail,
            gst = passgst,
            gsT_Number = passgstnumber,
            gsTHolderName =passgstholdername ,
            gsTAddress = passgstaddress,
            costCenterId = 0,
            projectId = 0,
            bookingRemark = "",
            corporateStatus = 0,
            corporatePaymentMode = 0,
            missedSavingReason = "",
            corpTripType = "",
            corpTripSubType = "",
            tripRequestId = "",
            bookingAlertIds = "",
            iPAddress = mStash?.getStringValue(Constants.deviceIPAddress, ""),
            requestId =  mStash?.getStringValue(Constants.requestId, ""),
            imeINumber = "2232323232323",
            registrationID = mStash?.getStringValue(Constants.MerchantId, ""),
            bookingFlightDetails = getBookingFlightDetails(),
            paX_Details = getpaxDetails()
        )

        Log.d("FlightTempBookingReq", Gson().toJson(flightTempBookingreq))

        viewModel.getFlightTempBookingRequest(flightTempBookingreq).observe(this) { resource ->
            resource?.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                pd.dismiss()
                                Log.d("FlightTempResponse", response.responseHeader.errorCode)
                                mStash!!.setStringValue(Constants.BookingRefNo,response.bookingRefNo)

                                if(response.responseHeader.errorCode.equals("0000")){
                                    getAllWalletBalance()
                                }

                                if(response.responseHeader.errorCode.equals("0004")){

                                    Toast.makeText(requireContext(),response.responseHeader.errorDesc,Toast.LENGTH_SHORT).show()
                                }

                                if(response.responseHeader.errorCode.equals("0006")){

                                    Toast.makeText(requireContext(),response.responseHeader.errorInnerException,Toast.LENGTH_SHORT).show()
                                }

                                Log.d("FlightRePriseResponse", Gson().toJson(response))

                            }
                        }
                    }

                    ApiStatus.ERROR -> {
                        pd.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        pd.show()
                    }
                }
            }
        }

    }


    private fun getAllWalletBalance() {
        requireContext().runIfConnected {
            val walletBalanceReq = GetBalanceReq(
                parmUser = mStash!!.getStringValue(Constants.RegistrationId, ""),
                flag = "CreditBalance"
            )
            getAllApiServiceViewModel.getWalletBalance(walletBalanceReq)
                .observe(viewLifecycleOwner) { resource ->
                    resource?.let {
                        when (it.apiStatus) {
                            ApiStatus.SUCCESS -> {
                                it.data?.let { users ->
                                    users.body()?.let { response ->
                                        getAllWalletBalanceRes(response)
                                    }
                                }
                            }

                            ApiStatus.ERROR -> {
                                pd.dismiss()
                            }

                            ApiStatus.LOADING -> {
                                pd.show()
                            }
                        }
                    }
                }
        }
    }


    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun getAllWalletBalanceRes(response: GetBalanceRes) {
        if (response.isSuccess == true) {
            val mainBalance = (response.data[0].result!!.toDoubleOrNull() ?: 0.0)
            val totalamoutFlightTicket =   mStash!!.getStringValue(Constants.AirTotalTicketPrice, "")!!.toDoubleOrNull() ?: 0.0

            if(totalamoutFlightTicket<= mainBalance){
                getMerchantBalance(totalamoutFlightTicket)
            }else{
                pd.dismiss()
                Toast.makeText(requireContext(), "Wallet balance is low. VBal = $mainBalance,  totalAmt = $totalamoutFlightTicket", Toast.LENGTH_LONG).show()
            }

            Log.d("actualBalance", "main = $mainBalance")

        } else {
            toast(response.returnMessage.toString())
        }

    }


    private fun getMerchantBalance(flightBalance: Double) {
        val getMerchantBalanceReq = GetMerchantBalanceReq(
            parmUser = mStash!!.getStringValue(Constants.MerchantId, "")/*"AOP-554"*/, // for testing purpose only
            flag = "DebitBalance"
        )
        getAllApiServiceViewModel.getAllMerchantBalance(getMerchantBalanceReq)
            .observe(viewLifecycleOwner) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            it.data?.let { users ->
                                users.body()?.let { response ->
                                    getAllMerchantBalanceRes(response, flightBalance)
                                }
                            }
                        }

                        ApiStatus.ERROR -> {
                            pd.dismiss()
                        }

                        ApiStatus.LOADING -> {
                            pd.show()
                        }
                    }
                }
            }
    }


    private fun getAllMerchantBalanceRes(response: GetMerchantBalanceRes, flightBalance: Double) {
        if (response.isSuccess == true) {
            Log.d(ContentValues.TAG, "getAllMerchantBalanceRes: ${response.data[0].debitBalance}")
            mStash!!.setStringValue(Constants.merchantBalance, response.data[0].debitBalance)

            val merchantBalance = response.data[0].debitBalance?.toDoubleOrNull() ?: 0.0
            val totalamoutFlightTicket =   mStash!!.getStringValue(Constants.AirTotalTicketPrice, "")!!.toDoubleOrNull() ?: 0.0

            if (totalamoutFlightTicket <= merchantBalance) {
               // skip few deduction flow .....................................................
                HitApiForFlightAddPayment()
            }
            else {
                pd.dismiss()
                Toast.makeText(requireContext(), "Booking is currently unavailable due to a technical issue. Please try again shortly.", Toast.LENGTH_LONG).show()
            }
        }
        else {
            pd.dismiss()
            Toast.makeText(requireContext(), response.returnMessage.toString(), Toast.LENGTH_SHORT)
                .show()
        }

    }


    fun HitApiForFlightAddPayment(){
        val flightaddpaymentreq = FlightAddPaymentReq(
            clientRefNo = "BosDevelopmentAddPayment",
            refNo =  mStash!!.getStringValue(Constants.BookingRefNo,""),
            transactionType = 0,
            productId = "1",
            iPAddress = mStash?.getStringValue(Constants.deviceIPAddress, ""),
            requestId =  mStash?.getStringValue(Constants.requestId, ""),
            imeINumber = "2232323232323",
            registrationID = mStash?.getStringValue(Constants.MerchantId, "")/*"AOP-554"*/
        )

        Log.d("AddPaymentReq", Gson().toJson(flightaddpaymentreq))

        viewModel.getFlightAddPaymentRequest(flightaddpaymentreq)
            .observe(viewLifecycleOwner) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            it.data?.let { users ->
                                users.body()?.let { response ->
                                    if(response.responseHeader.errorCode.equals("0000")) {
                                        Log.d("AddPaymentResponse",response.paymentID)
                                        hitApiForAirTicketing()
                                    }else{
                                        pd.dismiss()
                                        Toast.makeText(requireContext(),response.responseHeader.errorInnerException,Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }

                        ApiStatus.ERROR -> {
                            pd.dismiss()
                        }

                        ApiStatus.LOADING -> {
                            pd.show()
                        }

                    }
                }
            }

    }


    fun hitApiForAirTicketing(){
        val airTicketingreq = AirTicketingReq(
            BookingRefNo = mStash!!.getStringValue(Constants.BookingRefNo,""),
            TicketingType =  "1",
            iPAddress = mStash?.getStringValue(Constants.deviceIPAddress, ""),
            requestId =  mStash?.getStringValue(Constants.requestId, ""),
            imeINumber = "2232323232323",
            registrationID = mStash?.getStringValue(Constants.MerchantId, "")/*"AOP-554"*/
        )

        Log.d("airticketreq", Gson().toJson(airTicketingreq))

        viewModel.getAirTicketingRequest(airTicketingreq)
            .observe(viewLifecycleOwner) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            it.data?.let { users ->
                                users.body()?.let { response ->

                                    Log.d("airticketingresponse",response.toString())

                                    if(response.responseHeader.statusId.equals("22")){
                                        Toast.makeText(context,response.responseHeader.errorInnerException,Toast.LENGTH_SHORT).show()
                                        pd.dismiss()

                                    }

                                    if(response.responseHeader.errorCode.equals("0000")){
                                        val airTicketReprintreq = AirReprintReq(
                                            BookingRefNo = response.bookingRefNo,
                                            airlinePNR =  response.airlinePNRDetails!![0].airlinePNRs!![0].airlinePNR,
                                            ipAddress = mStash?.getStringValue(Constants.deviceIPAddress, ""),
                                            requestId =  mStash?.getStringValue(Constants.requestId, ""),
                                            imeNumber = "2232323232323",
                                            registerId = mStash?.getStringValue(Constants.MerchantId, "")/*"AOP-554"*/
                                        )
                                       /* Log.d("airTicketReprintreq",Gson().toJson(airTicketReprintreq))*/
                                        hitApiForTicketReprint(airTicketReprintreq)
                                    }
                                    else{
                                        pd.dismiss()
                                        Toast.makeText(context,response.responseHeader.errorInnerException,Toast.LENGTH_SHORT).show()
                                    }


                                }
                            }
                        }

                        ApiStatus.ERROR -> {
                            pd.dismiss()
                        }

                        ApiStatus.LOADING -> {
                            pd.show()
                        }

                    }
                }
            }

    }


    fun hitApiForTicketReprint(airReprintTicketReq:AirReprintReq){

        Log.d("ticketreprintreq", Gson().toJson(airReprintTicketReq))

        viewModel.getAirTicketReprintRequest(airReprintTicketReq)
            .observe(viewLifecycleOwner) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            it.data?.let { users ->
                                users.body()?.let { response ->
                                    BookedTicketList=response

                                    var requeryReq = FlightRequeryReq(
                                        loginId = mStash!!.getStringValue(Constants.RegistrationId, ""),
                                        bookingRefNo = response.bookingRefNo,
                                        ipAddress = mStash?.getStringValue(Constants.deviceIPAddress, ""),
                                        requestId = mStash?.getStringValue(Constants.requestId, ""),
                                        imeinumber = "2232323232323",
                                        registrationID = mStash!!.getStringValue(Constants.RegistrationId, ""),
                                        airPnr = response.airPNRDetails!![0]?.airlinePNR,
                                        flightNumber = response.airPNRDetails[0]!!.flights!![0].flightId, //flight_Id
                                        travelDate = response.airPNRDetails[0]!!.flights!![0].travelDate,
                                        ticketStatusId = response.airPNRDetails[0]!!.ticketStatusId,
                                        ticketStatusDesc = response.airPNRDetails[0]!!.ticketStatusDesc,
                                        apiResponse = Gson().toJson(response),
                                        createdBy = mStash!!.getStringValue(Constants.RegistrationId, ""),
                                        destination = response.airPNRDetails[0]!!.flights!![0].destination,
                                        origin = response.airPNRDetails[0]!!.flights!![0].origin
                                     )

                                    Log.d("ticketrequeryreq",Gson().toJson(requeryReq))
                                    hitApiForRequeryRequest(requeryReq)
                                    Log.d("ticketreprintresponse",response.bookingRefNo)

                                }
                            }
                        }

                        ApiStatus.ERROR -> {
                            pd.dismiss()
                        }

                        ApiStatus.LOADING -> {
                            pd.show()
                        }

                    }
                }
            }
    }


    fun getBookingFlightDetails() : MutableList<BookingFlightDetails>{
        var flightsearchkey = mStash!!.getStringValue(Constants.FlightSearchKey,"")
        var flightkey = mStash?.getStringValue(Constants.FlightKey,"")
        var bookingFlightDetails: MutableList<BookingFlightDetails> = mutableListOf()
        var bookingSSRLits : MutableList<BookingSSRDetails> = mutableListOf()
        bookingFlightDetails.clear()

        bookingFlightDetails.add(
            BookingFlightDetails(searchKey = flightsearchkey.toString(),
                flightKey = flightkey,
                bookingSSRDetails = bookingSSRLits,
                )
        )

        return bookingFlightDetails
    }


    fun getpaxDetails(): MutableList<PaXDetailsFlight> {
        val paxDetailsList = mutableListOf<PaXDetailsFlight>()

        passangerDetailsList.forEachIndexed { index, passenger ->

            val paxType = when (passenger.passangerType.uppercase()) {
                "ADULT" -> 0
                "CHILD" -> 1
                "INFANT" -> 2
                else -> 0 // fallback default
            }

            val genderCode = when (passenger.gender.trim().lowercase()) {
                "male" -> 0
                "female" -> 1
                else -> 0 // default to Male
            }

            // Basic validation (adjust as needed)
            if (passenger.firstName.isNullOrBlank() || passenger.lastName.isNullOrBlank()) {
                // Skip invalid entries
                return@forEachIndexed
            }

            val pax = PaXDetailsFlight(
                pax_Id = index + 1, // 1-based index
                paxtype = paxType,
                title = passenger.title ?: "",
                firstName = passenger.firstName ?: "",
                lastName = passenger.lastName ?: "",
                gender = genderCode,
                age = "", // optional, can calculate if needed
                dob = passenger.dob ?: "",
                passportNumber = passenger.passportno ?: "",
                passportIssuingCountry = passenger.passportissuecountryname ?: "",
                passportExpiry = passenger.passportexpirydate ?: "",
                nationality = "",
                frequentFlyerDetails = ""
            )

            paxDetailsList.add(pax)
        }

        return paxDetailsList
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
       // (activity as? FlightMainActivity)?.setData()

        if(context is FlightMainActivity){
            (context as? FlightMainActivity)?.setData()
        }
        else {
            (scanForActivity(context)?.supportFragmentManager?.findFragmentByTag("FlightMainFragment") as? FlightMainFragment)?.setData()
        }

    }


    fun hitApiForRequeryRequest(requeryReq : FlightRequeryReq){
        getAllApiServiceViewModel.getAirRequeryRequest(requeryReq)
            .observe(viewLifecycleOwner) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            it.data?.let { users ->
                                users.body()?.let { response ->
                                    pd.dismiss()
                                    Constants.uploadDataOnFirebaseConsole(Gson().toJson(response),"ReviewDetailsBottomSheetRequeryRequest",requireContext())

                                    Log.d("ticketrequeryresp",Gson().toJson(requeryReq))
                                    startActivity(Intent(requireContext(),FlightBookedTicketActivity::class.java))
                                    Toast.makeText(context,"Reprint Success",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        ApiStatus.ERROR -> {
                            pd.dismiss()
                        }

                        ApiStatus.LOADING -> {
                            pd.show()
                        }

                    }
                }
            }
        }




}