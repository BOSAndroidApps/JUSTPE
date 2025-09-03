package com.bos.payment.appName.ui.view.travel.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bos.payment.appName.ui.view.travel.airfragment.CancelledRefundFlight
import com.bos.payment.appName.ui.view.travel.airfragment.CompletedAir
import com.bos.payment.appName.ui.view.travel.airfragment.UpcomingAir
import com.bos.payment.appName.ui.view.travel.busfragment.CancelledRefundBus
import com.bos.payment.appName.ui.view.travel.busfragment.CompletedBus
import com.bos.payment.appName.ui.view.travel.busfragment.UpcomingBus
private const val NUM_TABS = 3

class FlightViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm,lifecycle) {


    override fun getItemCount(): Int {
        return NUM_TABS
    }


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UpcomingAir()
            1-> CancelledRefundFlight()
            2 -> CompletedAir()
            else -> UpcomingBus()
        }



    }


}