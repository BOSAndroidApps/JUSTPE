package com.bos.payment.appName.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bos.payment.appName.ui.view.makepayment.fragment.MakePaymentReports
import com.bos.payment.appName.ui.view.makepayment.fragment.RaiseMakePayment

private const val NUM_TABS = 2
class MakePaymentAdapter (fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm,lifecycle) {


    override fun getItemCount(): Int {
        return NUM_TABS
    }


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RaiseMakePayment()
            1-> MakePaymentReports()
            else -> RaiseMakePayment()
        }

    }

}