package com.korkmazanil.studentgradeentrywithroomdbandfirestore.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Student
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: Student)

    @Transaction
    @Query("DELETE FROM student_table")
    suspend fun deleteAllStudents()

    @Transaction
    @Query("SELECT *FROM student_table ORDER BY studentFullName ASC")
    fun getAlpabetizeStudents() : Flow<List<Student>>

    @Transaction
    @Query("SELECT *FROM student_table ORDER BY studentRegisteredTime ASC")
    fun getCurrentTimeStudents() : Flow<List<Student>>

    @Query("SELECT *FROM student_table WHERE studentFullName LIKE :search")
    fun findStudentName(search : String) : Flow<List<Student>>
}