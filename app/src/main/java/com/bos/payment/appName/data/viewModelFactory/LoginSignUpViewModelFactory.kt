package com.bos.payment.appName.data.viewModelFactory

import com.bos.payment.appName.data.repository.LoginSignUpRepository
import com.bos.payment.appName.ui.viewmodel.LoginSignUpViewModel

class LoginSignUpViewModelFactory constructor(private val repository: LoginSignUpRepository):
    androidx.lifecycle.ViewModelProvider.Factory {

        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(LoginSignUpViewModel::class.java)) {
                LoginSignUpViewModel(this.repository) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
}