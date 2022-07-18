package com.korkmazanil.studentgradeentrywithroomdbandfirestore.dao

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
    @Query("SELECT *FROM student_table order by studentFullName ASC")
    fun getAlpabetizeStudents() : Flow<List<Student>>

    @Transaction
    @Query("SELECT *FROM student_table order by studentRegisteredTime ASC")
    fun getCurrentTimeStudents() : Flow<List<Student>>
}