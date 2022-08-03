package com.korkmazanil.studentgradeentrywithroomdbandfirestore.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "lesson_table")
data class Lesson(
    @PrimaryKey
    var lessonId : String ="",
    var lessonStudentId : String ="",
    var lessonName : String="",
    var lessonMidTermExam : String="",
    var lessonFinalExam : String="",
    var lessonAverageGrade : String="",

    var studentFullName : String=""

) : Serializable{

    companion object{
        const val FIELD_LESSON_ID = "lessonId"
        const val FIELD_LESSON_STUDENT_ID = "lessonStudentId"
        const val FIELD_LESSON_NAME = "lessonName"
        const val FIELD_LESSON_MIDTERM_EXAM = "lessonMidTermExam"
        const val FIELD_LESSON_FINAL_EXAM = "lessonFinalExam"
        const val FIELD_LESSON_AVERAGE_GRADE = "lessonAverageGrade"
        const val FIELD_LESSON_STUDENT_FULL_NAME = "studentFullName"
    }
}