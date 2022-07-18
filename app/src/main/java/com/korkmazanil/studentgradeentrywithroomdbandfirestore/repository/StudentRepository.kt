package com.korkmazanil.studentgradeentrywithroomdbandfirestore.repository

import androidx.annotation.WorkerThread
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.dao.StudentDao
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Student
import kotlinx.coroutines.flow.Flow

class StudentRepository(private val studentDao: StudentDao) {

    val allStudents : Flow<List<Student>> = studentDao.getAlpabetizeStudents()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(student : Student){
        studentDao.insert(student)
    }
}