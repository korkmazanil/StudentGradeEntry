package com.korkmazanil.studentgradeentrywithroomdbandfirestore.dao

import androidx.room.*
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Lesson
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lesson: Lesson)

    @Delete
    suspend fun deleteSelectLesson(lesson: Lesson)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(lesson: Lesson)

    @Transaction
    @Query("DELETE FROM student_table")
    suspend fun deleteAllLessons()

    @Transaction
    @Query("SELECT *FROM lesson_table order by lessonName ASC")
    fun getAlpabetizeLessons() : Flow<List<Lesson>>

    @Transaction
    @Query("SELECT *FROM lesson_table order by lessonName ASC")
    fun getStudentWithLessons() : Flow<List<Lesson>>
}