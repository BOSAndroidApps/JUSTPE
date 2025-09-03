package com.bos.payment.appName.data.viewModelFactory

import com.bos.payment.appName.data.repository.PayoutRepository
import com.bos.payment.appName.ui.viewmodel.PayoutViewModel

class PayoutViewModelFactory constructor(private val repository: PayoutRepository):
    androidx.lifecycle.ViewModelProvider.Factory {

    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PayoutViewModel::class.java)) {
            PayoutViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}