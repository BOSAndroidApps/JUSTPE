package com.bos.payment.appName.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.bos.payment.appName.data.repository.AttendanceRepository2

class AttendanceViewModel2(application: Application) : AndroidViewModel(application){
    var attendanceRepository: AttendanceRepository2? = AttendanceRepository2()

//      fun login(StrLoginID: String?, StrUserPassword: String?,StrCompanycode:String?) = attendanceRepository!!.login(StrLoginID, StrUserPassword,StrCompanycode)

}