package com.korkmazanil.studentgradeentrywithroomdbandfirestore

import android.app.Application
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.repository.LessonRepository
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.repository.StudentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class GradeApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { GradeDatabase.getDatabase(this,applicationScope) }
    val studentRepository by lazy { StudentRepository(database.studentDao) }
    val lessonRepository by lazy { LessonRepository(database.lessonDao) }
    }

