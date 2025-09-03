package com.bos.payment.appName.data.viewModelFactory

import com.bos.payment.appName.data.repository.MoneyTransferRepository
import com.bos.payment.appName.ui.viewmodel.MoneyTransferViewModel

class MoneyTransferViewModelFactory (private val repository: MoneyTransferRepository):
    androidx.lifecycle.ViewModelProvider.Factory {

    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MoneyTransferViewModel::class.java)) {
            MoneyTransferViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}