package com.korkmazanil.studentgradeentrywithroomdbandfirestore.dao

import androidx.room.*
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Lesson
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lesson: Lesson)

    @Delete
    suspend fun deleteSelectLesson(lesson: Lesson) : Int

    @Update
    suspend fun update(lesson: Lesson)

    @Transaction
    @Query("DELETE FROM lesson_table")
    suspend fun deleteAllLessons()

    @Transaction
    @Query("SELECT *FROM lesson_table order by lessonName ASC")
    fun getAlpabetizeLessons() : Flow<List<Lesson>>

    @Transaction
    @Query("SELECT *FROM lesson_table order by lessonName ASC")
    fun getStudentWithLessons() : Flow<List<Lesson>>

//    @Query("SELECT * FROM lesson_table WHERE lessonName LIKE :search ")
//    fun findLessonName(search: String): List<Lesson>
}