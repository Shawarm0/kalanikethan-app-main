package com.lra.kalanikethan.data.repository

import com.lra.kalanikethan.data.models.Employee
import com.lra.kalanikethan.data.remote.SupabaseClientProvider
import com.lra.kalanikethan.util.Tables
import io.github.jan.supabase.postgrest.from

class EmployeeRepository {
    private val client = SupabaseClientProvider.client

    suspend fun getAllEmployees(): List<Employee> {
        return client.from(Tables.EMPLOYEES).select().decodeList<Employee>()
    }
}
