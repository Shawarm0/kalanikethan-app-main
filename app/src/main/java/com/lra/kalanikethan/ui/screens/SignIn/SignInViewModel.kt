package com.lra.kalanikethan.ui.screens.SignIn

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethan.data.models.Channel
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.data.remote.SupabaseClientProvider.client
import com.lra.kalanikethan.data.repository.ChannelManager
import kotlinx.coroutines.flow.MutableStateFlow
import com.lra.kalanikethan.data.repository.Repository
import com.lra.kalanikethan.data.repository.manager
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlin.collections.map


class SignInViewModel(
    private val repository: Repository
): ViewModel() {

    private val _allStudents = MutableStateFlow<List<Student>>(emptyList())
    val allStudents: StateFlow<List<Student>> = _allStudents

    private val _displayedStudents = MutableStateFlow<List<Student>>(emptyList())
    val displayedStudents: StateFlow<List<Student>> = _displayedStudents
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery
    private var debounceJob: Job? = null

    private lateinit var channel : Channel
    private lateinit var dataFlow : Flow<PostgresAction>

    init {
        viewModelScope.launch {
            manager.unsubscribeFromAllChannels()
            channel = manager.subscribeToStudentsChannel()
            dataFlow = channel.getFlow()
        }
    }


    
    fun initializeStudentChannel(){
        viewModelScope.launch {
            channel = manager.subscribeToStudentsChannel()
            dataFlow = channel.getFlow()
        }
    }
    
    fun initializeStudents() {
        viewModelScope.launch {
            _allStudents.value = repository.getAllStudents()
            filterStudents()
            
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            withContext(Dispatchers.Unconfined) {
                filterStudents()
            }
        }
    }

    fun filterStudents() {
        val query = _searchQuery.value.lowercase().trim()
        if (query.isEmpty()) {
            _displayedStudents.value = _allStudents.value.sortedBy { it.firstName.lowercase() }
        } else {
            _displayedStudents.value = _allStudents.value.filter { student ->
                student.firstName.lowercase().contains(query) || student.lastName.lowercase().contains(query) || student.studentId.toString() == query
            }.sortedBy { it.firstName.lowercase() }
        }
    }

    fun createStudentChannel(scope: CoroutineScope) {
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(1)
            
           dataFlow.collect { action ->
                when(action) {
                    is PostgresAction.Delete -> TODO()
                    is PostgresAction.Insert -> TODO()
                    is PostgresAction.Select -> TODO()
                    is PostgresAction.Update -> {
                        Log.i("Database", "Received action from students channel: $action")
                        val student = Json.decodeFromJsonElement<Student>(action.record)
                        _allStudents.update { list ->
                            list.map { if (it.studentId == student.studentId) student else it }
                        }
                        filterStudents()
                    }
                }
            }
        }
    }


}
