package com.lra.kalanikethencic.ui.screens.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lra.kalanikethencic.data.model.Class
import com.lra.kalanikethencic.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _allClasses = MutableStateFlow<List<Class>>(emptyList())
    val allClasses: StateFlow<List<Class>> = _allClasses


    fun preloadClasses() {
        if (_allClasses.value.isEmpty()) {
            loadClasses()
        }
    }

    private fun loadClasses() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getAllClasses()
                    .collect { classList ->
                        _allClasses.value = classList
                    }
            }
        }
    }

    fun getStudentsById(classId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getStudentsForClass(classId)
                    .collect { studentList ->
                        println(studentList)
                    }
            }
        }
    }


}