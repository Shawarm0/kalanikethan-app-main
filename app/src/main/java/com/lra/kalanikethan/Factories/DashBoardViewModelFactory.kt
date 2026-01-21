package com.lra.kalanikethan.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lra.kalanikethan.data.repository.Repository
import com.lra.kalanikethan.ui.screens.dashBoardViewModel.DashBoardViewModel
import com.lra.kalanikethan.ui.screens.signIn.SignInViewModel

class DashBoardViewModelFactory(
    private val repository: Repository,
    private val signInViewModel: SignInViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DashBoardViewModel(repository, signInViewModel) as T
    }
}