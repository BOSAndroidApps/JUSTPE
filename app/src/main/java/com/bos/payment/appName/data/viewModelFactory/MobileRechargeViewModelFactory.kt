package com.bos.payment.appName.data.viewModelFactory

import com.bos.payment.appName.data.repository.LoginSignUpRepository
import com.bos.payment.appName.data.repository.MobileRechargeRepository
import com.bos.payment.appName.ui.viewmodel.GetAllMobileRechargeViewModel
import com.bos.payment.appName.ui.viewmodel.LoginSignUpViewModel

class MobileRechargeViewModelFactory constructor(private val repository: MobileRechargeRepository):
    androidx.lifecycle.ViewModelProvider.Factory {

    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GetAllMobileRechargeViewModel::class.java)) {
            GetAllMobileRechargeViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}