package com.bos.payment.appName.data.viewModelFactory

import com.bos.payment.appName.data.repository.TravelRepository
import com.bos.payment.appName.ui.viewmodel.TravelViewModel


class TravelViewModelFactory constructor(private val repository: TravelRepository): androidx.lifecycle.ViewModelProvider.Factory {

    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TravelViewModel::class.java)) {
            TravelViewModel(this.repository) as T
        }
        else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}