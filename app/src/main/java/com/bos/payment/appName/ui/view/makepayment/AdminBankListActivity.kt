package com.bos.payment.appName.ui.view.makepayment

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.makepaymentnew.BankDataItem
import com.bos.payment.appName.data.model.makepaymentnew.BankDetailsReq
import com.bos.payment.appName.data.model.makepaymentnew.ReferenceIDGenerateReq
import com.bos.payment.appName.data.repository.GetAllAPIServiceRepository
import com.bos.payment.appName.data.viewModelFactory.GetAllApiServiceViewModelFactory
import com.bos.payment.appName.databinding.ActivityAdminBankListBinding
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.adapter.BankListAdapter
import com.bos.payment.appName.ui.viewmodel.GetAllApiServiceViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.google.gson.Gson

class AdminBankListActivity : AppCompatActivity() {
    lateinit var binding : ActivityAdminBankListBinding
    private lateinit var getAllApiServiceViewModel: GetAllApiServiceViewModel
    private  var bankList : List<BankDataItem?>? = arrayListOf()
    var filteredList: MutableList<BankDataItem?> = mutableListOf()
    lateinit var pd: ProgressDialog
    private var mStash: MStash? = null
    lateinit var adapter : BankListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBankListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAllApiServiceViewModel = ViewModelProvider(this, GetAllApiServiceViewModelFactory(
            GetAllAPIServiceRepository(RetrofitClient.apiAllInterface)))[GetAllApiServiceViewModel::class.java]

        mStash = MStash.getInstance(this)
        pd = ProgressDialog(this)
        hitApiForAdminBankList()
        setonclicklistner()

    }

    private fun setonclicklistner(){

        binding.back.setOnClickListener {
            finish()
        }

        binding.holdername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                filterBankList(query, binding.accountnumber.text.toString().trim())
            }
        })

        binding.accountnumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                filterBankList(binding.holdername.text.toString().trim(), query)
            }
        })
    }

    private fun filterBankList(holderName: String, accountNumber: String) {
        if (bankList.isNullOrEmpty()) return

        filteredList = bankList!!.filter { item ->
            val matchesHolder = holderName.isEmpty() || item?.accountHolderName?.contains(holderName, ignoreCase = true) == true
            val matchesAccount = accountNumber.isEmpty() || item?.accountNo?.contains(accountNumber, ignoreCase = true) == true
            matchesHolder && matchesAccount
        }.toMutableList()

        // 👉 Update your RecyclerView adapter here
        if(filteredList.isNotEmpty()){
            binding.banklist.visibility= View.VISIBLE
            binding.notfoundlayout.visibility= View.GONE
            adapter.updateList(filteredList)
        }
        else{
            binding.banklist.visibility= View.GONE
            binding.notfoundlayout.visibility= View.VISIBLE
        }

    }

    fun hitApiForAdminBankList(){

        var req = BankDetailsReq(
            parmFlag = "SHOW",
            accountNo = "",
            accountHolderName = "",
            adminCode = mStash!!.getStringValue(Constants.AdminCode, "")
        )

        getAllApiServiceViewModel.getbanklistreq(req).observe(this) { resource ->
            resource?.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("banklistresp", Gson().toJson(response))
                                if(response.isSuccess!!){
                                    pd.dismiss()
                                    bankList= response.data
                                    if(bankList!!.isNotEmpty()){
                                        setAdapterData(bankList)
                                    }else{
                                        binding.banklist.visibility= View.GONE
                                        binding.notfoundlayout.visibility= View.VISIBLE
                                    }

                                } else{
                                    Toast.makeText(this,response.returnMessage, Toast.LENGTH_SHORT).show()
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

    private fun setAdapterData( bankList : List<BankDataItem?>?){
        if(bankList!!.isNotEmpty()){
            binding.banklist.visibility= View.VISIBLE
            binding.notfoundlayout.visibility= View.GONE
            adapter = BankListAdapter(this@AdminBankListActivity,bankList)
            binding.banklist.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        else {
            binding.banklist.visibility= View.GONE
            binding.notfoundlayout.visibility= View.VISIBLE
        }

    }


}