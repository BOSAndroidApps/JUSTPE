package com.bos.payment.appName.data.viewModelFactory

import com.bos.payment.appName.data.repository.SubmitOTPRepository
import com.bos.payment.appName.ui.viewmodel.SubmitOTPViewModel

class SubmitOTPViewModelFactory constructor(private val repository: SubmitOTPRepository) :
    androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SubmitOTPViewModel::class.java)) {
            SubmitOTPViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}