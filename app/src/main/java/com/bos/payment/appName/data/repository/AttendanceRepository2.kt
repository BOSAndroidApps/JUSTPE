package com.bos.payment.appName.data.repository

import com.bos.payment.appName.network.RetrofitClient

class AttendanceRepository2 {
    val userDataService2 = RetrofitClient.getService2()

//    fun login(StrUserName: String?, StrPassword: String?, StrCompanycode:String?): LiveData<LoginModel?> {
//        val mutableLiveData = MutableLiveData<LoginModel?>()
//        val call = userDataService2!!.login(StrUserName, StrPassword,StrCompanycode)
//        call!!.enqueue(object : Callback<LoginModel?> {
//            override fun onResponse(call: Call<LoginModel?>, resp: Response<LoginModel?>) {
//                if (resp.body() != null && resp.isSuccessful && resp.body()!!.Status == ConstantClass.Success) {
//                    mutableLiveData.value =resp.body()
//                }else if (resp.body() != null && resp.isSuccessful) {
//                    mutableLiveData.value = LoginModel(resp.body()!!.Status)
//                }  else {
//                    mutableLiveData.value = LoginModel(ConstantClass.api_response_default_Message)
//                }
//            }
//            override fun onFailure(call: Call<LoginModel?>, t: Throwable) {
//                t.printStackTrace()
//                mutableLiveData.value = LoginModel(t.localizedMessage)
//            }
//        })
//        return mutableLiveData
//    }
}