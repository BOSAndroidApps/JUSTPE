package com.bos.payment.appName.data.viewModelFactory

import com.bos.payment.appName.data.repository.ReportsRepository
import com.bos.payment.appName.ui.viewmodel.ReportsViewModel

class ReportsViewModelFactory constructor(private val repository: ReportsRepository):
    androidx.lifecycle.ViewModelProvider.Factory {

    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ReportsViewModel::class.java)) {
            ReportsViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}