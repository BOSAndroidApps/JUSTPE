package com.bos.payment.appName.utils

import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.recharge.operator.Data
import com.bos.payment.appName.data.model.recharge.operator.RechargeOperatorsListReq
import com.bos.payment.appName.data.repository.MoneyTransferRepository
import com.bos.payment.appName.data.viewModelFactory.MoneyTransferViewModelFactory
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.viewmodel.MoneyTransferViewModel

class GetDropDown {
    private var mStash: MStash? = null
    private lateinit var viewModel: MoneyTransferViewModel

    init {

        Constants.operatorName = ArrayList()
        Constants.dthName = ArrayList()

        Constants.operatorNameMap = HashMap()
        Constants.dthNameMap = HashMap()

        Constants.operatorNameMapForGettingOperatorName = HashMap()
        Constants.dthNameMapForGettingDthName = HashMap()

        if (!Constants.operatorName!!.contains("Select Operator")) Constants.operatorName!!.add(
            0,
            "Select Operator"
        )
    }

    fun dropDownAllValues(context: FragmentActivity) {
        Constants.getAllOperatorAdapter = ArrayAdapter<String>(
            context,
            R.layout.spinner_item_layout,
            Constants.operatorName!!
        )


        mStash = MStash.getInstance(context)
        viewModel = ViewModelProvider(
            context,
            MoneyTransferViewModelFactory(MoneyTransferRepository(RetrofitClient.apiAllInterface))
        )[MoneyTransferViewModel::class.java]

        val rechargeOperatorsListReq = RechargeOperatorsListReq(
            registrationID = mStash!!.getStringValue(Constants.MerchantId, ""),
            RechargeType = "Online"
        )
        viewModel.getAllOperatorList(rechargeOperatorsListReq).observe(context) {
            it?.let { resource ->
                when (resource.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        resource.data?.let { x ->
                            //    if(Constants.dropDownValues?.size!=0)Constants.dropDownValues?.clear()
//                            Constants.dropDownValues = x.body()
                            x.body()?.let { it1 -> populateDropDowns(it1.data) }
                        }
                    }

                    ApiStatus.ERROR -> {
                        Toast.makeText(context, "try again", Toast.LENGTH_SHORT).show()
                    }

                    ApiStatus.LOADING -> {
                    }
                }
            }
        }

    }

    private fun populateDropDowns(list: ArrayList<Data>) {
        for (singleObject in list) {
            when (singleObject.category) {
                "Prepaid" -> {
                    Constants.operatorName!!.add(singleObject.name.toString())
                    Constants.operatorNameMap!![singleObject.name.toString()] =
                        Integer.valueOf(singleObject.id?.toInt() ?: 0)
                    Constants.operatorNameMapForGettingOperatorName!![singleObject.id?.toInt()
                        ?: 0] = singleObject.name.toString()
                }

                "DTH" -> {
                    Constants.dthName!!.add(singleObject.name.toString())
                    Constants.dthNameMap!![singleObject.name.toString()] =
                        Integer.valueOf(singleObject.id?.toInt() ?: 0)
                    Constants.dthNameMapForGettingDthName!![singleObject.id?.toInt()
                        ?: 0] = singleObject.name.toString()
                }
            }
        }
    }

}
