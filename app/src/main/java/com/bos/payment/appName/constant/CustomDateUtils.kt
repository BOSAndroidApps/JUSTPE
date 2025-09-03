package com.bos.payment.appName.constant

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object CustomDateUtils {
    const val dateFormat = "yyyy-MM-dd"
    const val dateFormatServer = "dd/MMM/yyyy"
    const val dateFormatServer2 = "yyyy-MM-dd'T'HH:mm:ss"
    const val dateFormatServer3 = "MMM dd, yyyy"
    const val dateFormatServer4 = "dd MMM yyyy"
    const val dateFormatServer5 = "hh:mm a"
    const val dateFormatServer6 = "yyyy-MM-dd"
    const val dateFormatServer7 = "yyyy-MM-dd HH:mm:ss"
    const val dateFormatServer8 = "dd-MMM-yyyy hh:mm"
    const val dateFormatServer9 = "dd/MM/yyyy"
    const val dateFormatServer10 = "dd-MM-yyyy"
    const val dateFormatServer11 = "yyyy-MM-dd"
    const val dateFormatServer12 = "yyyy-MM-dd HH:mm:ss.SSS"


    fun showDatePickerDialogMaxDt(context: Context?, dateFormat: String?, callBack: ISC_String) {
        val myCalendar = Calendar.getInstance()
        val date =
            DatePickerDialog.OnDateSetListener { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                myCalendar[Calendar.HOUR_OF_DAY] = 0
                myCalendar[Calendar.MINUTE] = 0
                myCalendar[Calendar.SECOND] = 0
                myCalendar[Calendar.MILLISECOND] = 0
                val dateString =
                    SimpleDateFormat(dateFormat, Locale.UK)
                        .format(myCalendar.time)
                callBack.onClicked(dateString)
            }
        val datePickerDialog = DatePickerDialog(
            context!!, date,
            myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH], myCalendar[Calendar.DAY_OF_MONTH]
        )
        val cMax = Calendar.getInstance()
        cMax[2004, 11] = 30
        val cMin = Calendar.getInstance()
        cMin[1947, 1] = 1

        //datePickerDialog.getDatePicker().setMinDate(cMin.getTimeInMillis());
        // datePickerDialog.getDatePicker().setMaxDate(cMax.getTimeInMillis());
        datePickerDialog.datePicker.maxDate = Date().time
        datePickerDialog.show()
    }

    fun showTimePickerDialog(context: Context?, is24Hours: Boolean, callBack: ISC_TimeString) {
        val calendar = Calendar.getInstance()
        val hours = calendar[Calendar.HOUR_OF_DAY]
        val minutes = calendar[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(context,
            { view: TimePicker?, hourOfDay: Int, minute: Int ->
                var hoursStr = ""
                var minStr = ""
                hoursStr = if (hourOfDay < 10) {
                    "0$hourOfDay"
                } else {
                    hourOfDay.toString() + ""
                }
                minStr = if (minute < 10) {
                    "0$minute"
                } else {
                    minute.toString() + ""
                }
                val am_pm = if (hourOfDay > 11) " PM " else " AM "
                val hh =
                    if (hourOfDay > 12) if (hourOfDay - 12 < 10) "0" + (hourOfDay - 12) else (hourOfDay - 12).toString() else if (hourOfDay < 10) "0$hourOfDay" else hourOfDay.toString()
                val mm = minStr
                callBack.onclick(hoursStr, minStr, "$hh:$mm$am_pm")
            }, hours, minutes, is24Hours
        )
        timePickerDialog.show()
    }

    fun getCurrentDate(): String {
        val c = Calendar.getInstance().time
        println("Current time => $c")
        val df =
            SimpleDateFormat(dateFormatServer9, Locale.getDefault())
        return df.format(c)
    }
//    val calendar = Calendar.getInstance()
//    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//    val currentDateTime = dateFormat.format(calendar.time)
//    println("Current Date and Time: $currentDateTime")

    fun getCurrentDate(format: String?): String? {
        val c = Calendar.getInstance().time
        println("Current time => $c")
        val df =
            SimpleDateFormat(format, Locale.getDefault())
        return df.format(c)
    }

    fun compareDateChanger(olddate: String): Boolean {
        var isDateChanger = false
        if (getCurrentDate().compareTo(changeDateFormat(olddate)!!) == 0) {
            isDateChanger = false
            Log.d("Return", "getTestTime less than getCurrentTime ")
        } else {
            isDateChanger = true
            Log.d("Return", "getTestTime older than getCurrentTime ")
        }
        return isDateChanger
    }

    private fun changeDateFormat(old_dateformat: String): String? {
        val inputFormat = SimpleDateFormat(dateFormatServer8)
        val outputFormat = SimpleDateFormat(dateFormatServer9)
        var date: Date? = null
        var str: String? = null
        try {
            date = inputFormat.parse(old_dateformat)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }

    fun changeDateFormat2(old_dateformat: String?): String? {
        val inputFormat = SimpleDateFormat(dateFormatServer7)
        val outputFormat = SimpleDateFormat(dateFormatServer5)
        var date: Date? = null
        var str: String? = null
        try {
            date = inputFormat.parse(old_dateformat)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }

    interface ISC_String {
        fun onClicked(string: String?)
    }

    interface ISC_TimeString {
        fun onclick(hours: String?, minuets: String?, TimeString: String?)
    }
}