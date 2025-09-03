package com.bos.payment.appName.data.viewModelFactory

import com.bos.payment.appName.data.repository.GetAllAPIServiceRepository
import com.bos.payment.appName.ui.viewmodel.GetAllApiServiceViewModel

class GetAllApiServiceViewModelFactory constructor(private val repository: GetAllAPIServiceRepository):
    androidx.lifecycle.ViewModelProvider.Factory {

    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GetAllApiServiceViewModel::class.java)) {
            GetAllApiServiceViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}