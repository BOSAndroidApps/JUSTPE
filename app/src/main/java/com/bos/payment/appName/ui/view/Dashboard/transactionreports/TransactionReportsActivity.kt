package com.bos.payment.appName.ui.view.Dashboard.transactionreports

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.transactionreportsmodel.CheckRaiseTicketExistReq
import com.bos.payment.appName.data.model.transactionreportsmodel.ReportListReq
import com.bos.payment.appName.data.model.transactionreportsmodel.TransactionReportsReq
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.GetBalanceReq
import com.bos.payment.appName.data.repository.GetAllAPIServiceRepository
import com.bos.payment.appName.data.viewModelFactory.GetAllApiServiceViewModelFactory
import com.bos.payment.appName.databinding.ActivityTransactionReportsBinding
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.adapter.TransactionReportAdapter
import com.bos.payment.appName.ui.viewmodel.GetAllApiServiceViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.bos.payment.appName.utils.Utils.runIfConnected
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class TransactionReportsActivity : AppCompatActivity() {
    lateinit var binding: ActivityTransactionReportsBinding
    private lateinit var getAllApiServiceViewModel: GetAllApiServiceViewModel
    lateinit var pd : ProgressDialog
    private var mStash: MStash? = null
    var displayReportList : MutableList<String?> = arrayListOf()
    var reportModeList : MutableList<String?> = arrayListOf()
    var displaytxtReportList : MutableList<String?> = arrayListOf()
    private val myCalender = Calendar.getInstance()
    private val myCalender1 = Calendar.getInstance()
    var FromDate: String= ""
    var ToDate: String =""
    lateinit var adapter : TransactionReportAdapter
    lateinit var dialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAllApiServiceViewModel = ViewModelProvider(this, GetAllApiServiceViewModelFactory(GetAllAPIServiceRepository(RetrofitClient.apiAllInterface))
        )[GetAllApiServiceViewModel::class.java]

        pd = ProgressDialog(this)
        mStash = MStash.getInstance(this)

        hitapiforReports()
        setClickListner()

    }


    private fun setClickListner(){

        binding.back.setOnClickListener {
            finish()
        }

        binding.fromDate.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    myCalender.set(year, monthOfYear, dayOfMonth)
                    binding.fromDate.text = "$dayOfMonth/$monthOfYear/$year"

                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    calendar.set(year, monthOfYear - 1, dayOfMonth, 0, 0, 0) // Example time
                    calendar.set(Calendar.MILLISECOND, 860)

                    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    sdf.timeZone = TimeZone.getTimeZone("UTC")

                    val isoString = sdf.format(calendar.time)
                    FromDate = isoString
                    if(binding.reportmode.selectedItem.toString().trim().isNotEmpty() && binding.selectreport.selectedItem.toString().trim().isNotEmpty()&& ToDate.isNotEmpty() && FromDate.isNotEmpty()){
                        hitApiForGettingTransactionReports()
                    }
                    Log.d("FromDate", FromDate)
                },
                myCalender.get(Calendar.YEAR),
                myCalender.get(Calendar.MONTH),
                myCalender.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.toDate.setOnClickListener {

            DatePickerDialog(
                this, { _, year, monthOfYear, dayOfMonth ->
                    myCalender1.set(year, monthOfYear, dayOfMonth)
                    binding.toDate.text = "$dayOfMonth/$monthOfYear/$year"

                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    calendar.set(year, monthOfYear - 1, dayOfMonth, 0, 0, 0) // Example time
                    calendar.set(Calendar.MILLISECOND, 860)

                    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    sdf.timeZone = TimeZone.getTimeZone("UTC")

                    val isoString = sdf.format(calendar.time)
                    ToDate = isoString
                    if(binding.reportmode.selectedItem.toString().trim().isNotEmpty() && binding.selectreport.selectedItem.toString().trim().isNotEmpty()&& ToDate.isNotEmpty() && FromDate.isNotEmpty()){
                        hitApiForGettingTransactionReports()
                    }
                    Log.d("ToDate", ToDate)
                },
                myCalender1.get(Calendar.YEAR),
                myCalender1.get(Calendar.MONTH),
                myCalender1.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.reportmode.onItemSelectedListener= object : OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selectedItem = binding.reportmode.selectedItem.toString().trim()

                if(selectedItem.isNotEmpty() && binding.selectreport.selectedItem.toString().trim().isNotEmpty()&& ToDate.isNotEmpty() && FromDate.isNotEmpty()){
                    hitApiForGettingTransactionReports()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.selectreport.onItemSelectedListener=object :OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selectedItem = binding.selectreport.selectedItem.toString().trim()

                if(selectedItem.isNotEmpty() && binding.reportmode.selectedItem.toString().trim().isNotEmpty()&& ToDate.isNotEmpty() && FromDate.isNotEmpty()){
                    hitApiForGettingTransactionReports()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

    }


    private fun hitapiforReports() {
        runIfConnected {
            val reportReq = ReportListReq(
                pParmFlag ="Reports" ,
                pParmFlag1 = mStash!!.getStringValue(Constants.RegistrationId, ""),
                pParmFlag2 = ""
            )

            getAllApiServiceViewModel.sendForReportListReq(reportReq)
                .observe(this) { resource ->
                    resource?.let {
                        when (it.apiStatus) {
                            ApiStatus.SUCCESS -> {
                                it.data?.let { users ->
                                    users.body()?.let { response ->
                                        if(response.isSuccess!!){
                                            pd.dismiss()
                                            response.data!!.forEach { unit->
                                                displayReportList.add(unit!!.displayValue)
                                                displaytxtReportList.add(unit!!.displayText)
                                                setReportInSpinner()
                                            }
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
    }


    private fun setReportInSpinner(){
        var adapter = ArrayAdapter(this@TransactionReportsActivity, android.R.layout.simple_spinner_dropdown_item, displayReportList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.selectreport.adapter = adapter
        setReportModeInSpinner()
    }


    private fun setReportModeInSpinner(){
        reportModeList.clear()
        reportModeList.add("Recharge History")
        reportModeList.add("Dmt History")
        var adapter = ArrayAdapter(this@TransactionReportsActivity, android.R.layout.simple_spinner_dropdown_item, reportModeList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.reportmode.adapter = adapter
    }


    private fun hitApiForGettingTransactionReports(){
        runIfConnected {
            val reportReq = TransactionReportsReq(
                parmFlag = binding.selectreport.selectedItem.toString().trim() ,
                paramMerchantCode = mStash!!.getStringValue(Constants.MerchantId,""),
                parmToDate = ToDate,
                parmUser = mStash!!.getStringValue(Constants.RegistrationId,""),
                parmFla2 = binding.reportmode.selectedItem.toString().trim(),
                parmFromDate = FromDate
            )
            Log.d("TransactionReportsReq", Gson().toJson(reportReq))

            getAllApiServiceViewModel.sendTransactionReportsReq(reportReq)
                .observe(this) { resource ->
                    resource?.let {
                        when (it.apiStatus) {
                            ApiStatus.SUCCESS -> {
                                it.data?.let { users ->
                                    users.body()?.let { response ->
                                        Log.d("TransactionReportsResp", Gson().toJson(response))
                                        pd.dismiss()
                                        if(response.isSuccess!!){
                                            response.data!!.forEach { unit->
                                                if(response.data.size>0){
                                                    binding.notfoundlayout.visibility= View.GONE
                                                    binding.reportslayout.visibility = View.VISIBLE
                                                    var datalist = response.data
                                                    adapter= TransactionReportAdapter(this,datalist)
                                                    binding.reportslayout.adapter=adapter
                                                    adapter.notifyDataSetChanged()
                                                }
                                                else{
                                                    binding.notfoundlayout.visibility= View.VISIBLE
                                                    binding.reportslayout.visibility = View.GONE
                                                }

                                            }
                                        }
                                        else{
                                            binding.notfoundlayout.visibility= View.VISIBLE
                                            binding.reportslayout.visibility = View.GONE
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
    }


     fun hitApiForCheckRaiseTicket(transactionID:String){
        runIfConnected {
            val reportReq = CheckRaiseTicketExistReq(
                transactionID = transactionID ,
            )
            Log.d("TransactionReportsReq", Gson().toJson(reportReq))

            getAllApiServiceViewModel.sendTransactionRaiseTicketExitsReq(reportReq)
                .observe(this) { resource ->
                    resource?.let {
                        when (it.apiStatus) {
                            ApiStatus.SUCCESS -> {
                                it.data?.let { users ->
                                    users.body()?.let { response ->
                                        Log.d("TransactionReportsResp", Gson().toJson(response))
                                        pd.dismiss()
                                        if(response.isSuccess!!){
                                          if(response.data!!.isValid!!){
                                            startActivity(Intent(this,RaiseTicketActivity::class.java))
                                          }else{
                                              PopOpForCibileScoreRequestToAdmin(transactionID)
                                          }
                                        }
                                        else{
                                          Toast.makeText(this,response.returnMessage,Toast.LENGTH_SHORT).show()
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
     }



    fun PopOpForCibileScoreRequestToAdmin(transactionId : String){
        dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.signoutalert)

        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        dialog.setCanceledOnTouchOutside(false)

        val done = dialog.findViewById<Button>(R.id.ok)
        val txt = dialog.findViewById<TextView>(R.id.dialog_message)
        val titletxt = dialog.findViewById<TextView>(R.id.title)

        txt.text="A ticket has already been raised by the retailer. Transaction No: $transactionId"

        done.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }


}