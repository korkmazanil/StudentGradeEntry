package com.korkmazanil.studentgradeentrywithroomdbandfirestore.model

import com.google.firebase.Timestamp
import java.io.Serializable

data class Teacher(
    var teacherId : String,
    var teacherName : String,
    var teacherEmailAddress : String,
    var teacherRegisteredTime: Timestamp
) : Serializable

