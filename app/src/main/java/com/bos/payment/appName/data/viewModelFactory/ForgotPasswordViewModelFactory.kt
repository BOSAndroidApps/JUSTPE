package com.bos.payment.appName.data.viewModelFactory

import com.bos.payment.appName.data.repository.ForgotPassWordRepository
import com.bos.payment.appName.ui.viewmodel.ForgotPasswordViewModel

class ForgotPasswordViewModelFactory constructor(private val repository: ForgotPassWordRepository) :
    androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java)) {
            ForgotPasswordViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}