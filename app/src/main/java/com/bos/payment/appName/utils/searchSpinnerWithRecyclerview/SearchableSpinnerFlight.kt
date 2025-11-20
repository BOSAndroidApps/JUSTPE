package com.bos.payment.appName.utils.searchSpinnerWithRecyclerview

import android.content.Context
import android.content.ContextWrapper
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.FragmentActivity
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.travel.flight.DataItem
import com.bos.payment.appName.ui.view.travel.adapter.AirPortListAdapter
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant
import com.bos.payment.appName.ui.view.travel.flightBooking.fragment.FlightMainFragment

class SearchableSpinnerFlight @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatSpinner(context, attrs, defStyleAttr), View.OnTouchListener {

    companion object {
        const val NO_ITEM_SELECTED = -1
    }

    private val _context: Context = context
    private val _items: MutableList<DataItem> = mutableListOf()
    private var _arrayAdapter: ArrayAdapter<DataItem>? = null
    private var _strHintText: String? = null
    private var _isFromInit = false
    private var _isDirty = false

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.SearchableSpinner_Style, 0, 0).apply {
            try {
                _strHintText = getString(R.styleable.SearchableSpinner_Style_hintText)
            } finally {
                recycle()
            }
        }
        setOnTouchListener(this)

        _arrayAdapter = adapter as? ArrayAdapter<DataItem>

        if (!TextUtils.isEmpty(_strHintText)) {
            val hintItem = DataItem("", _strHintText ?: "", "", "")
            val hintAdapter = ArrayAdapter(_context, android.R.layout.simple_list_item_1, arrayOf(hintItem))
            _isFromInit = true
            setAdapter(hintAdapter)
        }
    }

    override fun setAdapter(adapter: SpinnerAdapter?) {
        if (!_isFromInit) {
            _arrayAdapter = adapter as? ArrayAdapter<DataItem>
            if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
                val hintItem = DataItem("", _strHintText ?: "", "", "")
                val hintAdapter = ArrayAdapter(_context, android.R.layout.simple_list_item_1, arrayOf(hintItem))
                super.setAdapter(hintAdapter)
            } else {
                super.setAdapter(adapter)
            }
        } else {
            _isFromInit = false
            super.setAdapter(adapter)
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            openDialog()

        }
        return true
    }


    fun openDialog() {
        FlightConstant.FlightList?.let { adapter ->
            _items.clear()
            for (i in 0 until adapter.size) {
                adapter[i]?.let { _items.add(it) }
            }

            val searchableDialog = SearchableListFlightSpinnerDialog(
                _context,
                _items,
                object : AirPortListAdapter.setClickListner {
                    override fun itemclickListner(position: Int,item:DataItem) {
                         _items.clear()
                        if (FlightConstant.checkFrom) {
                            FlightConstant.fromCityName = item.city
                            FlightConstant.fromAirportCode =item.airportCode
                            FlightConstant.fromAirportName = item.airportDescription
                            FlightConstant.fromCountryName = item.country
                        } else {
                            FlightConstant.toCityName = item.city
                            FlightConstant.toAirportCode =item.airportCode
                            FlightConstant.toAirportName = item.airportDescription
                            FlightConstant.toCountryName = item.country
                        }
                        /*if(context is FlightMainActivity){
                            (context as? FlightMainActivity)?.setData()
                        }
                        else {*/
                            (scanForActivity(_context)?.supportFragmentManager?.findFragmentByTag("FlightMainFragment") as? FlightMainFragment)?.setData()
                        /*}*/
                    }
                }

            )

            scanForActivity(_context)?.fragmentManager?.let {
                searchableDialog.show(it, "SearchableSpinner")
            }
        }
    }


    private fun scanForActivity(cont: Context?): FragmentActivity? {
        return when (cont) {
            null -> null
            is FragmentActivity -> cont
            is ContextWrapper -> scanForActivity(cont.baseContext)
            else -> null
        }
    }


    override fun getSelectedItemPosition(): Int {
        return if (!TextUtils.isEmpty(_strHintText) && !_isDirty) NO_ITEM_SELECTED else super.getSelectedItemPosition()
    }

    override fun getSelectedItem(): Any? {
        return if (!TextUtils.isEmpty(_strHintText) && !_isDirty) null else super.getSelectedItem()
    }

}
