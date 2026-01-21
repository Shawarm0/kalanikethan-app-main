package com.lra.kalanikethan.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lra.kalanikethan.data.repository.Repository
import com.lra.kalanikethan.ui.screens.signIn.SignInViewModel

class SignInViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignInViewModel(repository) as T
    }
}
