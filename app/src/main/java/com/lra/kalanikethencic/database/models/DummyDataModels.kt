package com.lra.kalanikethencic.database.models

data class Family(
    val familyId: Int,
    val familyName: String,
    val email: String,
    val familyPaymentId: String
)

data class Parent(
    val parentId: Int,
    val familyId: Int,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String
)

data class Student(
    val studentId: Int,
    val familyId: Int,
    val firstName: String,
    val lastName: String,
    val birthdate: String,
    val canWalkAlone: Boolean,
    val dance: Boolean,
    val singing: Boolean,
    val music: Boolean,
    val signedIn: Boolean
)

data class Employee(
    val employeeId: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val manager: Boolean
)

data class ClassItem(
    val classId: Int,
    val teacherName: String,
    val type: String,
    val startTime: Long,
    val endTime: Long
)

data class StudentClass(
    val studentId: Int,
    val classId: Int
)

data class History(
    val historyId: Int,
    val studentId: Int,
    val classId: Int,
    val day: String,
    val signInTime: Long,
    val signOutTime: Long,
    val employeeId: Int
)

data class PaymentHistory(
    val paymentId: Int,
    val familyId: Int,
    val date: String,
    val amount: Float,
    val paid: Boolean
)
