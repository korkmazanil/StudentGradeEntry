package com.korkmazanil.studentgradeentrywithroomdbandfirestore

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.dao.LessonDao
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.dao.StudentDao
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Lesson
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Student::class,
        Lesson::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GradeDatabase : RoomDatabase() {

    abstract val studentDao : StudentDao
    abstract val lessonDao : LessonDao

    companion object{
        @Volatile
        private var INSTANCE : GradeDatabase? = null

        fun getDatabase(context : Context,scope: CoroutineScope) : GradeDatabase{
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    GradeDatabase::class.java,
                    "grade_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(GradeDatabaseCallBack(scope))
                    .build().also {
                        INSTANCE = it
                    }
            }
        }
    }

    private class GradeDatabaseCallBack(private val scope : CoroutineScope) : RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.studentDao, database.lessonDao)
                }
            }
        }

        suspend fun populateDatabase(studentDao: StudentDao, lessonDao: LessonDao) {
            studentDao.deleteAllStudents()
            lessonDao.deleteAllLessons()
        }
    }
}