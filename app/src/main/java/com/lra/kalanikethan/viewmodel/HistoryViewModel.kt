package com.lra.kalanikethan.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethan.data.models.Class
import com.lra.kalanikethan.data.models.Employee
import com.lra.kalanikethan.data.models.History
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.data.remote.ChannelManager
import com.lra.kalanikethan.data.repository.EmployeeRepository
import com.lra.kalanikethan.data.repository.HistoryRepository
import com.lra.kalanikethan.util.Tables
import com.lra.kalanikethan.util.PdfExporter
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive
import java.time.LocalDate

class HistoryViewModel(
    private val historyRepository: HistoryRepository,
    private val employeeRepository: EmployeeRepository
) : ViewModel() {

    private val _allHistories = MutableStateFlow<List<History>>(emptyList())
    val histories: StateFlow<List<History>> = _allHistories

    private val _allEmployees = MutableStateFlow<List<Employee>>(emptyList())
    val employees: StateFlow<List<Employee>> = _allEmployees

    init {
        viewModelScope.launch {
            _allHistories.value = historyRepository.getAllHistories()
            _allEmployees.value = employeeRepository.getAllEmployees()
            subscribeToHistoryChanges()
        }
    }

    private fun subscribeToHistoryChanges() {
        viewModelScope.launch {
            val channel = ChannelManager.historyChannel()
            val changes = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                table = Tables.HISTORY
            }
            channel.subscribe()

            changes.collect { action ->
                when (action) {
                    is PostgresAction.Insert -> {
                        val history = Json.decodeFromJsonElement<History>(action.record)
                        _allHistories.update { list -> list + history }
                    }
                    is PostgresAction.Update -> {
                        val history = Json.decodeFromJsonElement<History>(action.record)
                        _allHistories.update { list ->
                            list.map { if (it.historyID == history.historyID) history else it }
                        }
                    }
                    is PostgresAction.Delete -> {
                        val deletedId = action.oldRecord["history_id"]?.jsonPrimitive?.intOrNull
                        if (deletedId != null) {
                            _allHistories.update { list -> list.filter { it.historyID != deletedId } }
                        }
                    }
                    else -> {
                        Log.i("HistorySync", "Unknown action: $action")
                    }
                }
            }
        }
    }

    fun exportHistoryPdf(from: LocalDate, to: LocalDate, students: List<Student>, classes: List<Class>) {
        viewModelScope.launch(Dispatchers.IO) {
            val filtered = _allHistories.value.filter {
                val date = LocalDate.parse(it.date)
                !date.isBefore(from) && !date.isAfter(to)
            }

            PdfExporter.exportHistory(
                histories = filtered,
                students = students,
                classes = classes,
                employees = _allEmployees.value
            )
        }
    }
}
