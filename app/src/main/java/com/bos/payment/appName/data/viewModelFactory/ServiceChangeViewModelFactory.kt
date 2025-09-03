package com.bos.payment.appName.data.viewModelFactory

import com.bos.payment.appName.data.repository.ServiceChangeRepository
import com.bos.payment.appName.ui.viewmodel.ServiceChangeViewModel

class ServiceChangeViewModelFactory constructor(private val repository: ServiceChangeRepository):
    androidx.lifecycle.ViewModelProvider.Factory {

    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ServiceChangeViewModel::class.java)) {
            ServiceChangeViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}