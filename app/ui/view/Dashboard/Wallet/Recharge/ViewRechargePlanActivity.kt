package com.bos.payment.appName.ui.view.Dashboard.Wallet.Recharge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.bos.payment.appName.databinding.ActivityViewRechargePlanBinding

class ViewRechargePlanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewRechargePlanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityViewRechargePlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        try {
//            rechargeType = intent.getStringExtra("rechargeType")!!
////            model = intent.getSerializableExtra("CircleListModelTopUp") as CircleListModelTopUp
//            model = intent.getSerializableExtra("CircleListModelTopUp") as TOPUP
//            binding!!.toolbar.tvToolbarName.text = "Plan Detail"
//            binding!!.rlTrans.visibility = View.GONE
//            binding!!.view.visibility = View.GONE
//            binding!!.tvType.setText("N/A")
//            binding!!.tvDataPack.setText("N/A")
//            if (model.rs == "") {
//                binding!!.tvRupee.setText("N/A")
//            } else {
//                binding!!.tvRupee.setText(model.rs)
//            }
//            if (model.validity == "") {
//                binding!!.tvValidity.setText("N/A")
//            } else {
//                binding!!.tvValidity.setText(model.validity)
//            }
//            if (model.lastUpdate == "") {
//                binding!!.tvValid.setText("N/A")
//            } else {
//                binding!!.tvValid.setText(model.lastUpdate)
//            }
//            if (model.desc == "") {
//                binding!!.tvDescription.setText("N/A")
//            } else {
//                binding!!.tvDescription.setText(model.desc)
//            }
//        } catch (e: Exception) {

        }
//        try {
//            model2 =
//                intent.getSerializableExtra("TransactionHistoryModelList") as RechargeHistoryRes
//            binding!!.toolbar.tvToolbarName.text = "Transaction History"
//            binding!!.tvDate.text = "Date"
//            binding!!.tvType.text = model2.DATE
//
//            binding!!.tvTime.text = "Time"
//            binding!!.tvRupee.text = model2.TIME
//
//            if (rechargeType == "DTH") {
//                binding!!.tvMobile.text = "CA No."
//            } else {
//                binding!!.tvMobile.text = "Mobile No."
//            }
//
//            binding!!.tvValidity.text = model2.MOBILENO
//
//            binding!!.tvOperator.text = "Operator"
//            binding!!.tvDataPack.text = model2.SERVICETYPE
//
//            binding!!.tvAmount.text = "Amount"
//            binding!!.tvValid.text = model2.TRANSACTIONAMT
//
//            binding!!.tvStatus.text = "Status"
//            binding!!.tvDescription.text = model2.STATUS
//
//            binding!!.tvRemarks.text = "Remarks"
//            binding!!.tvRemarkResult.text = model2.REMARKS
//        } catch (e: Exception) {

//        }
//        binding!!.toolbar.ivBack.setOnClickListener { onBackPressed() }


//    }
}