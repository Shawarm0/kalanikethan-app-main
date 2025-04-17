package com.lra.kalanikethencic.database.respository

import com.lra.kalanikethencic.database.models.*
import javax.inject.Inject

class DummyRepository @Inject constructor() {

    fun getFamilies(): List<Family> = listOf(
        Family(1, "Smith Family", "smith@example.com", "PAY123"),
        Family(2, "Johnson Family", "johnson@example.com", "PAY456")
    )

    fun getParents(): List<Parent> = listOf(
        Parent(1, 1, "John", "Smith", "1234567890"),
        Parent(2, 2, "Alice", "Johnson", "0987654321")
    )

    fun getStudents(): List<Student> = listOf(
        Student(1, 1, "Emily", "Smith", "19-10-2005", true, true, false, false, true),
        Student(2, 2, "Jake", "Johnson", "10-02-2006", false, false, true, true, false)
    )

    fun getEmployees(): List<Employee> = listOf(
        Employee(1, "Tom", "Holland", "tom@school.com", "password123", true),
        Employee(2, "Linda", "Lee", "linda@school.com", "admin456", false)
    )

    fun getClasses(): List<ClassItem> = listOf(
        ClassItem(1, "Tom Holland", "Dance", 1682006400000, 1682010000000),
        ClassItem(2, "Linda Lee", "Music", 1682013600000, 1682017200000)
    )

    fun getStudentClasses(): List<StudentClass> = listOf(
        StudentClass(1, 1),
        StudentClass(2, 2)
    )

    fun getHistory(): List<History> = listOf(
        History(1, 1, 1, "2025-04-15", 1682006500000, 1682010000000, 1),
        History(2, 2, 2, "2025-04-15", 1682013600000, 1682017200000, 2)
    )

    fun getPaymentHistory(): List<PaymentHistory> = listOf(
        PaymentHistory(1, 1, "2025-04-01", 50.0f, true),
        PaymentHistory(2, 2, "2025-04-01", 50.0f, false)
    )
}
