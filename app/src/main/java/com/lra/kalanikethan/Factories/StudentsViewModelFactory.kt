package com.lra.kalanikethan.Factories

import com.lra.kalanikethan.StudentsViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lra.kalanikethan.data.repository.Repository

class StudentsViewModelFactory(
    private val repository: Repository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StudentsViewModel(repository) as T
    }
}