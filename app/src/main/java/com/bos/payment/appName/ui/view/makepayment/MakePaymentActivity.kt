package com.bos.payment.appName.ui.view.makepayment

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.bos.payment.appName.R
import com.bos.payment.appName.databinding.ActivityMakePayment2Binding
import com.bos.payment.appName.databinding.ActivityMakePaymentBinding
import com.bos.payment.appName.ui.adapter.MakePaymentAdapter
import com.bos.payment.appName.utils.MStash
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MakePaymentActivity : AppCompatActivity() {
    lateinit var binding : ActivityMakePayment2Binding
    private var mStash: MStash? = null
    val statusArray = listOf("Make Pay", "Reports")
    lateinit var  viewPager: ViewPager2
    lateinit var  tabLayout: TabLayout

    companion object{
        var BankName : String = ""
        var BankAccountNumber : String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMakePayment2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setView()
        setOnClickListner()

    }

    private fun setOnClickListner(){

        binding.back.setOnClickListener {
            finish()
        }

    }

    private fun setView(){

        viewPager = binding.viewPager
        tabLayout = binding.tablayout

        val adapter = MakePaymentAdapter(supportFragmentManager, lifecycle)
        viewPager.isUserInputEnabled = true
        viewPager.adapter = adapter


        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val tabView =  LayoutInflater.from(tabLayout.context).inflate(R.layout.tab_title, null)
            val text=tabView.findViewById<TextView>(R.id.tabText)
            text.text = statusArray[position]
            tab.customView= tabView
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val textView = tab.customView as? TextView
                textView?.isSelected = true // triggers ColorStateList
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val textView = tab.customView as? TextView
                textView?.isSelected = false
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        // Also mark the initially selected tab (0)
        (tabLayout.getTabAt(tabLayout.selectedTabPosition)?.customView as? TextView)?.isSelected = true

    }
}