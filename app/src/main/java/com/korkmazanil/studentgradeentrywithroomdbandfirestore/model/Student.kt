package com.korkmazanil.studentgradeentrywithroomdbandfirestore.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "student_table")
data class Student(
    @PrimaryKey
    var studentId : String,
    var studentFullName : String,
    var studentDepartment : String,
    var studentRegisteredTime : String
) : Serializable {
    companion object{
        const val FIELD_STUDENT_ID = "studentId"
        const val FIELD_STUDENT_FULL_NAME = "studentFullName"
        const val FIELD_STUDENT_DEPARTMENT = "studentDepartment"
        const val FIELD_STUDENT_REGISTERED_TIME = "studentRegisteredTime"
    }
}




