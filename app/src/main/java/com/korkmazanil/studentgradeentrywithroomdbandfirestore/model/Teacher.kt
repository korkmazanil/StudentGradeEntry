package com.korkmazanil.studentgradeentrywithroomdbandfirestore.model

import com.google.firebase.Timestamp
import java.io.Serializable

data class Teacher(
    val teacherId : String,
    val teacherName : String,
    val teacherEmailAddress : String,
    val teacherRegisteredTime: Timestamp
) : Serializable

