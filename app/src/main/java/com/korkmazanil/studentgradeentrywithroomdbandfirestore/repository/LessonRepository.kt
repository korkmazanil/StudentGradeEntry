package com.korkmazanil.studentgradeentrywithroomdbandfirestore.repository

import androidx.annotation.WorkerThread
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.dao.LessonDao
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Lesson
import kotlinx.coroutines.flow.Flow

class LessonRepository(private val lessonDao: LessonDao) {

    val allLessons : Flow<List<Lesson>> = lessonDao.getAlpabetizeLessons()
    val studentWithLessons : Flow<List<Lesson>> = lessonDao.getStudentWithLessons()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(lesson: Lesson){
        lessonDao.insert(lesson)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(lesson: Lesson){
        lessonDao.update(lesson)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(lesson: Lesson){
        lessonDao.deleteAllLessons()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun  deleteLesson(lesson: Lesson){
        lessonDao.deleteSelectLesson(lesson)
    }

//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    suspend fun  findLessonName(search: String){
//        lessonDao.findLessonName(search)
//    }

}