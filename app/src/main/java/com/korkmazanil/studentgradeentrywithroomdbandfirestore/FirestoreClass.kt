package com.korkmazanil.studentgradeentrywithroomdbandfirestore

import android.util.Log
import android.view.View
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Lesson
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Lesson.Companion.FIELD_LESSON_ID
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Lesson.Companion.FIELD_LESSON_NAME
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Student
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Student.Companion.FIELD_STUDENT_FULL_NAME
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Student.Companion.FIELD_STUDENT_ID
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Teacher
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.utils.Constants.LESSONS
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.utils.Constants.STUDENTS
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.utils.Constants.TEACHERS
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.view.activity.SignInActivity
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.view.activity.SignUpActivity
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.view.fragment.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class
FirestoreClass {

    private val mFirestore = Firebase.firestore
    private lateinit var studentList : ArrayList<Student>
    private lateinit var lessonList : ArrayList<Lesson>

    fun registerUser(activity: SignUpActivity, userInfo : Teacher){
        mFirestore.collection(TEACHERS)
            .document(getCurrentUserID())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
            .addOnFailureListener {e->
                Log.e(activity.javaClass.simpleName,"Error writing document -> ${e.localizedMessage}")
            }
    }

    fun signInUser(activity : SignInActivity){
        mFirestore.collection(TEACHERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener {
                activity.signInSuccess()
            }
            .addOnFailureListener {e->
                Log.e("SignInUser","Error getting document",e)
            }
    }

    fun addStudent(uuid : UUID, fragment : AddStudentFragment, studentInfo : Student, view : View){
        mFirestore
            .collection(TEACHERS)
            .document(getCurrentUserID())
            .collection(STUDENTS)
            .document(uuid.toString())
            .set(studentInfo, SetOptions.merge())
            .addOnSuccessListener { fragment.saveFirestoreStudentInfo(view, uuid) }
            .addOnFailureListener { e-> Log.e("AddStudent","Error setting document ${e.localizedMessage}") }
    }

    fun getStudents(fragment : StudentsListFragment){
        studentList = ArrayList()

        mFirestore
            .collection(TEACHERS)
            .document(getCurrentUserID())
            .collection(STUDENTS)
            .orderBy(FIELD_STUDENT_FULL_NAME,Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    if (!snapshot.isEmpty){

                        val documents = snapshot.documents
                        for (document in documents){
                            val studentId = document.get(FIELD_STUDENT_ID).toString()
                            val studentName = document.get(FIELD_STUDENT_FULL_NAME).toString()
                            val studentDepartment = document.get(Student.FIELD_STUDENT_DEPARTMENT).toString()
                            val studentRegisteredTime = document.get(Student.FIELD_STUDENT_REGISTERED_TIME).toString()

                            val student = Student(studentId,studentName,studentDepartment,studentRegisteredTime)
                            studentList.add(student)
                            fragment.getStudents(studentList)
                        }
                    }
                    else if (exception != null){
                        exception.localizedMessage?.let { Log.e("getStudents", it) }
                    }
                }
            }
    }

    //fun deleteStudent(){}

    fun addLesson(uuid: String, fragment : AddLessonFragment, lessonInfo : Lesson, view: View){
        mFirestore
            .collection(TEACHERS)
            .document(getCurrentUserID())
            .collection(LESSONS)
            .document(uuid)
            .set(lessonInfo, SetOptions.merge())
            .addOnSuccessListener { fragment.saveFirestoreLessonInfo(view,uuid) }
            .addOnFailureListener { e-> Log.e("AddLesson","Error setting document ${e.localizedMessage}") }
    }

    fun updateLesson(uuid: String, fragment: AddLessonFragment, lessonInfo: HashMap<String, Any>, view: View){
        mFirestore
            .collection(TEACHERS)
            .document(getCurrentUserID())
            .collection(LESSONS)
            .document(uuid)
            .set(lessonInfo, SetOptions.merge())
            .addOnSuccessListener { fragment.saveFirestoreLessonInfo(view,uuid) }
            .addOnFailureListener { e-> Log.e("UpdateLesson","Error setting document ${e.localizedMessage}") }
    }

    fun getLessons(fragment : LessonsListFragment){
        lessonList = ArrayList()

        mFirestore
            .collection(TEACHERS)
            .document(getCurrentUserID())
            .collection(LESSONS)
            .orderBy(FIELD_LESSON_NAME,Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    if (!snapshot.isEmpty){
                        val documents = snapshot.documents
                        for (document in documents){
                            val lessonId = document.get(FIELD_LESSON_ID).toString()
                            val lessonStudentId = document.get(Lesson.FIELD_LESSON_STUDENT_ID).toString()
                            val lessonName = document.get(FIELD_LESSON_NAME).toString()
                            val lessonMidtermExam = document.get(Lesson.FIELD_LESSON_MIDTERM_EXAM).toString().trim()
                            val lessonFinalExam = document.get(Lesson.FIELD_LESSON_FINAL_EXAM).toString().trim()
                            val lessonAverageGrade = document.get(Lesson.FIELD_LESSON_AVERAGE_GRADE).toString().trim()
                            val lessonStudentFullName = document.get(Lesson.FIELD_LESSON_STUDENT_FULL_NAME).toString()

                            val lesson = Lesson(lessonId,lessonStudentId,lessonName,
                                lessonMidtermExam, lessonFinalExam,
                                lessonAverageGrade,lessonStudentFullName)

                            lessonList.add(lesson)
                            fragment.getLessons(lessonList)
                        }
                    }
                    else if (exception != null){
                        exception.localizedMessage?.let { Log.e("getStudents", it) }
                    }
                }
            }
    }

    fun getStudentsByLesson(fragment : LessonsListFragment, fieldLessonName : String){
        lessonList = ArrayList()

        mFirestore
            .collection(TEACHERS)
            .document(getCurrentUserID())
            .collection(LESSONS)
            .whereEqualTo(FIELD_LESSON_NAME,fieldLessonName)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    if (!snapshot.isEmpty){
                        val documents = snapshot.documents
                        for (document in documents){
                            val lessonId = document.get(FIELD_LESSON_ID).toString()
                            val lessonStudentId = document.get(Lesson.FIELD_LESSON_STUDENT_ID).toString()
                            val lessonName = document.get(FIELD_LESSON_NAME).toString()
                            val lessonMidtermExam = document.get(Lesson.FIELD_LESSON_MIDTERM_EXAM).toString().trim()
                            val lessonFinalExam = document.get(Lesson.FIELD_LESSON_FINAL_EXAM).toString().trim()
                            val lessonAverageGrade = document.get(Lesson.FIELD_LESSON_AVERAGE_GRADE).toString().trim()
                            val lessonStudentFullName = document.get(Lesson.FIELD_LESSON_STUDENT_FULL_NAME).toString()

                            val lesson = Lesson(lessonId,lessonStudentId,lessonName,
                                lessonMidtermExam, lessonFinalExam,
                                lessonAverageGrade,lessonStudentFullName)

                            lessonList.add(lesson)
                            fragment.getStudentsByLesson(lessonList)
                        }
                    }
                    else if (exception != null){
                        exception.localizedMessage?.let { Log.e("getStudents", it) }
                    }
                }
            }
    }

    fun deleteLesson(clickedLessonList : ArrayList<Lesson>, position : Int){
        val mFirestore = Firebase.firestore
        mFirestore.collection(TEACHERS)
            .document(getCurrentUserID())
            .collection(LESSONS)
            .whereEqualTo(FIELD_LESSON_ID,clickedLessonList[position].lessonId)
            .get()
            .addOnSuccessListener {
                val myBatch = mFirestore.batch()
                it.forEach { queryDocumentSnapshot ->
                    myBatch.delete(queryDocumentSnapshot.reference)
                }
                myBatch.commit()
            }
            .addOnFailureListener {
                Log.e("deleteLesson","Error, not deleted lesson")
            }
    }

    private fun getCurrentUserID() : String{
        return Firebase.auth.currentUser!!.uid
    }
}