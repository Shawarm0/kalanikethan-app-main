package com.lra.kalanikethan.data.repository


import android.util.Log
import androidx.compose.runtime.MutableState
import com.lra.kalanikethan.data.models.Class
import com.lra.kalanikethan.data.models.Employee
import com.lra.kalanikethan.data.models.Family
import com.lra.kalanikethan.data.models.FamilyPayments
import com.lra.kalanikethan.data.models.FamilyWithID
import com.lra.kalanikethan.data.models.History
import com.lra.kalanikethan.data.models.Parent
import com.lra.kalanikethan.data.models.PaymentHistory
import com.lra.kalanikethan.data.models.PaymentPlan
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.data.models.StudentClass
import com.lra.kalanikethan.data.models.sessionPermissions
import com.lra.kalanikethan.data.remote.SupabaseClientProvider
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

class Repository {
    // Defines the client
    private val client = SupabaseClientProvider.client

    suspend fun getLastFamilyID(): Int {
        val count = client.from("families")
            .select {
                count(Count.EXACT)
            }
            .countOrNull()!!
        return count.toInt()
    }

    suspend fun getPlanFromID(id : String) : PaymentPlan{
        return client.from("payment_plan").select {
            filter {
                PaymentPlan::family_payment_id eq id
            }
        }.decodeSingle<PaymentPlan>()
    }

    suspend fun updateClass(updatedClass: Class) {
        println(updatedClass)
        client.from("classes").upsert(updatedClass) {
            onConflict = "class_id"
        }
    }

    suspend fun addFirstFamilyPayment(paymentID : String, date : LocalDate, amount : Float){
        client.from("payment_history").insert(PaymentHistory(family_payment_id = paymentID, paid = false, due_date = date, amount = amount))
    }

    suspend fun getFamilyFromID(id : String) : FamilyWithID{
        return client.from("families").select {
            filter {
                FamilyWithID::familyID eq id
            }
        }.decodeSingle<FamilyWithID>()
    }

    suspend fun getUnpaidPayments() : List<PaymentHistory>{
        return client.from("payment_history").select{
            order(column = "due_date", order = Order.ASCENDING)
            filter {
                PaymentHistory::paid eq false
            }
        }.decodeList<PaymentHistory>()
    }

    suspend fun confirmPayment(id : Int, amount : Float){
        client.from("payment_history").update(
            {
                PaymentHistory::paid setTo true
                PaymentHistory::amount setTo amount
            }
        ){
            filter {
                PaymentHistory::payment_id eq id
            }
        }
    }

    suspend fun getLatestPaymentFromFamilyID(id : String) : PaymentHistory{
        Log.i("Database", "Filtering history with ID : $id")
        return client.from("payment_history").select {
            filter {
                PaymentHistory::family_payment_id eq id
            }
            order(column = "due_date", order = Order.DESCENDING)
        }.decodeSingle<PaymentHistory>()
    }

    suspend fun addPaymentToFamily(payment : PaymentHistory, familyID: String){
        val amount = getPlanFromID(familyID)
        payment.due_date = LocalDate(payment.due_date.year, payment.due_date.month, amount.payment_date)
        payment.due_date = payment.due_date.plus(1, DateTimeUnit.MONTH)
        payment.amount = amount.amount
        payment.paid = false
        payment.payment_id = null
        client.from("payment_history").insert(payment)
    }

    suspend fun getFamilyPaymentHistory(familyID : String) : List<PaymentHistory>{
        return client.from("payment_history").select {
            filter {
                PaymentHistory::family_payment_id eq familyID
            }
            order(column = "due_date", order = Order.DESCENDING)
        }.decodeList<PaymentHistory>()
    }

    suspend fun signInStudent(student: Student, history: History) {
        Log.i("Repository-SignIn", "Student: $student")
        try {
            client.from("students").upsert(student) {
                onConflict = "student_id"
            }

            client.from("history").insert(history)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun signOutStudent(student: Student, history: History?) {
        Log.i("Repository-SignOut", "Student: $student")
        try {
            client.from("students").upsert(student) {
                onConflict = "student_id"
            }

            if (history != null) {

                val updatedHistory = history.copy( signOutTime = System.currentTimeMillis(), uid = sessionPermissions.value.uid )
                client.from("history").upsert(updatedHistory) {
                    onConflict = "history_id"
                }
            } else {
                Log.i("Repository-SignOut", "Student history not found")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getAllClasses(): List<Class> {
        return client.from("classes").select().decodeList<Class>()
    }

    suspend fun getAllEmployees(): List<Employee> {
        return client.from("employees").select().decodeList<Employee>()
    }

    suspend fun getAllHistories(): List<History> {
        return client.from("history").select().decodeList<History>()
    }

    suspend fun queryNullHistories(): List<History> {
        return client.from("history").select().decodeList<History>().filter {
            it.signOutTime == null
        }
    }

    suspend fun getStudentIdsForClass(classID: Int): List<Int> {
        return client.from("student_classes").select() {
            filter {
                StudentClass::classId eq classID
            }
        }.decodeList<StudentClass>().map { it.studentId }
    }



//    suspend fun updateClassState(classID: Int, studentIDs: Set<Int>) {
//        Log.i("Repository-updateClassState", "Class ID: $classID, has students $studentIDs")
//        val existingStudentClass = client.from("student_classes").select() {
//            filter {
//                StudentClass::classId eq classID
//            }
//
//        }.decodeList<StudentClass>().map { it.studentId }
//
//        val studentsToAdd = studentIDs.filter { !existingStudentClass.contains(it) }
//        val studentsToRemove = existingStudentClass.filter { !studentIDs.contains(it) }
//
//        Log.i("Repository-updateClassState", "Students to add: $studentsToAdd")
//        Log.i("Repository-updateClassState", "Students to remove: $studentsToRemove")
//
//        for (studentID in studentsToAdd) {
//            client.from("student_classes").insert(StudentClass(studentID, classID))
//        }
//
//        for (studentID in studentsToRemove) {
//            client.from("student_classes").delete {
//                filter {
//                    StudentClass::classId eq classID
//                    StudentClass::studentId eq studentID
//                }
//            }
//        }
//
//    }


    suspend fun addStudentToClass(studentId: Int, classId: Int) {
        client.from("student_classes").insert(
            mapOf(
                "student_id" to studentId,
                "class_id" to classId
            )
        )
    }

    suspend fun removeStudentFromClass(studentId: Int, classId: Int) {
        client.from("student_classes")
            .delete {
                filter {
                    StudentClass::classId eq classId
                    StudentClass::studentId eq studentId
                }
            }
    }





    suspend fun getAllStudents(): List<Student> {
        val students = client.from("students").select().decodeList<Student>()
        return students
    }

    suspend fun addFamily(family: Family): String? {
        client.from("families").insert(family)
        val familyID = client.from("families").select(){
            filter {
                FamilyWithID::familyName eq family.familyName
                FamilyWithID::email eq family.email
            }
        }.decodeSingle<FamilyWithID>()
        return familyID.familyID
    }

    suspend fun addStudent(student: Student) {
        client.from("students").insert(student)
    }

    suspend fun addParent(parent: Parent) {
        client.from("parents").insert(parent)
    }

    suspend fun addPaymentData(paymentPlan: PaymentPlan){
        client.from("payment_plan").insert(paymentPlan)
    }
}