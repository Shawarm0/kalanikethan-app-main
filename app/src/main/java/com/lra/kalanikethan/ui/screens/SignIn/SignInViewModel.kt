package com.lra.kalanikethan.ui.screens.SignIn

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.data.remote.SupabaseClientProvider.client
import kotlinx.coroutines.flow.MutableStateFlow
import com.lra.kalanikethan.data.repository.Repository
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement


class SignInViewModel(
    private val repository: Repository
): ViewModel() {

    val allstudents = MutableStateFlow<List<Student>>(emptyList())
    private var debounceJob: Job? = null

    fun updateStudents(scope: CoroutineScope) {
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(1)
            repository.initializeStudentChannel(scope).collect { action ->
                when(action) {
                    is PostgresAction.Delete -> TODO()
                    is PostgresAction.Insert -> TODO()
                    is PostgresAction.Select -> TODO()
                    is PostgresAction.Update -> {
                        Log.i("Database", "${action.record}")
                    }
                }
            }
        }
    }


}
