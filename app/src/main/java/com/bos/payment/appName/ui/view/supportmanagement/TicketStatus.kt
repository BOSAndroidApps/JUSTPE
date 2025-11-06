package com.bos.payment.appName.ui.view.supportmanagement

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.R
import com.bos.payment.appName.constant.ConstantClass.saveImageToCache
import com.bos.payment.appName.data.model.supportmanagement.TicketStatusReq
import com.bos.payment.appName.data.model.supportmanagement.TicketsItem
import com.bos.payment.appName.data.model.transactionreportsmodel.RaiseTicketReq
import com.bos.payment.appName.data.repository.GetAllAPIServiceRepository
import com.bos.payment.appName.data.viewModelFactory.GetAllApiServiceViewModelFactory
import com.bos.payment.appName.databinding.ActivityTicketStatusBinding
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.adapter.TicketStatusAdapter
import com.bos.payment.appName.ui.view.Dashboard.transactionreports.RaiseTicketActivity.ImageAdapter
import com.bos.payment.appName.ui.viewmodel.GetAllApiServiceViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.bos.payment.appName.utils.Utils.runIfConnected
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class TicketStatus : AppCompatActivity() {
    lateinit var binding:ActivityTicketStatusBinding
    private lateinit var getAllApiServiceViewModel: GetAllApiServiceViewModel
    lateinit var pd: ProgressDialog
    private var mStash: MStash? = null
    lateinit var adapter : TicketStatusAdapter
    private val myCalender = Calendar.getInstance()
    private val myCalender1 = Calendar.getInstance()

    var FromDate: Date? = null
    var ToDate: Date? = null
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    var ticketstatuslist : MutableList<TicketsItem?>? = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTicketStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)


        getAllApiServiceViewModel = ViewModelProvider(this, GetAllApiServiceViewModelFactory(GetAllAPIServiceRepository(RetrofitClient.apiAllInterface))
        )[GetAllApiServiceViewModel::class.java]

        mStash = MStash.getInstance(this@TicketStatus)
        pd = ProgressDialog(this)

        sdf.timeZone = TimeZone.getTimeZone("UTC")

        hitApiForRaiseTicket()
        setclicklistner()

    }

    fun setclicklistner() {

        binding.back.setOnClickListener {
            finish()
        }

        binding.fromDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(year, monthOfYear, dayOfMonth)
                    binding.fromDate.text = "$dayOfMonth/${monthOfYear + 1}/$year"

                    // Set start of the day (00:00:00)
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)

                    FromDate = calendar.time

                    Log.d("FromDate", sdf.format(FromDate!!))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.toDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(year, monthOfYear, dayOfMonth)
                    binding.toDate.text = "$dayOfMonth/${monthOfYear + 1}/$year"

                    // End of the day (23:59:59)
                    calendar.set(Calendar.HOUR_OF_DAY, 23)
                    calendar.set(Calendar.MINUTE, 59)
                    calendar.set(Calendar.SECOND, 59)
                    calendar.set(Calendar.MILLISECOND, 999)

                    ToDate = calendar.time

                    Log.d("ToDate", sdf.format(ToDate!!))


                    if (FromDate != null && ToDate != null) {
                        val filteredList = ticketstatuslist!!.filter { comment ->
                            val commentDate = sdf.parse(comment!!.createdDate)
                            commentDate != null && !commentDate.before(FromDate) && !commentDate.after(ToDate)
                        }
                        if(filteredList.isNotEmpty()){
                            binding.showticketstatuslist.visibility= View.VISIBLE
                            binding.notfoundlayout.visibility=View.GONE
                            adapter = TicketStatusAdapter(this,filteredList)
                            binding.showticketstatuslist.adapter= adapter
                            adapter.notifyDataSetChanged()
                        }
                        else{
                            binding.showticketstatuslist.visibility= View.GONE
                            binding.notfoundlayout.visibility=View.VISIBLE

                        }
                        Log.d("FilteredSize", filteredList.size.toString())


                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

    }


    fun hitApiForRaiseTicket() {
        var userCode = mStash!!.getStringValue(Constants.RegistrationId, "").toString()
        var adminCode = mStash!!.getStringValue(Constants.AdminCode, "").toString()

        runIfConnected {
            val request = TicketStatusReq(
                userCode = userCode,
                adminCode = adminCode,
            )

            Log.d("ticketstatusreq", Gson().toJson(request))

            getAllApiServiceViewModel.sendTicketStatusReq(request).observe(this) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            it.data?.let { users ->
                                users.body()?.let { response ->
                                    Log.d("ticketstatusresp", Gson().toJson(response))
                                    pd.dismiss()
                                    if (response.isSuccess!!) {
                                        ticketstatuslist = response.data!!.tickets
                                        if(ticketstatuslist!!.isNotEmpty()&& !ticketstatuslist.isNullOrEmpty()){
                                            binding.showticketstatuslist.visibility= View.VISIBLE
                                            binding.notfoundlayout.visibility=View.GONE
                                            adapter = TicketStatusAdapter(this,ticketstatuslist)
                                            binding.showticketstatuslist.adapter= adapter
                                            adapter.notifyDataSetChanged()
                                        }
                                        else{
                                            binding.showticketstatuslist.visibility= View.GONE
                                            binding.notfoundlayout.visibility=View.VISIBLE
                                        }
                                    } else {
                                        Toast.makeText(this, response.returnMessage, Toast.LENGTH_SHORT).show()
                                        binding.showticketstatuslist.visibility= View.GONE
                                        binding.notfoundlayout.visibility=View.VISIBLE
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

}