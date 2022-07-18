package com.korkmazanil.studentgradeentrywithroomdbandfirestore.view.fragment

import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.findFragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.FirestoreClass
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.GradeApplication
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.R
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.databinding.FragmentAddLessonBinding
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Lesson
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Lesson.Companion.FIELD_LESSON_AVERAGE_GRADE
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Lesson.Companion.FIELD_LESSON_FINAL_EXAM
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Lesson.Companion.FIELD_LESSON_ID
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Lesson.Companion.FIELD_LESSON_MIDTERM_EXAM
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Lesson.Companion.FIELD_LESSON_NAME
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Lesson.Companion.FIELD_LESSON_STUDENT_FULL_NAME
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Lesson.Companion.FIELD_LESSON_STUDENT_ID
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.utils.Constants.UPDATE_PROCESS
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.viewmodel.LessonViewModel
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.viewmodel.LessonViewModelFactory
import java.util.*

class AddLessonFragment : Fragment() {

    private lateinit var binding : FragmentAddLessonBinding

    private val args: AddLessonFragmentArgs by navArgs()
    private val lessonViewModel: LessonViewModel by viewModels {
        LessonViewModelFactory((activity?.application as GradeApplication).lessonRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_lesson, container, false)
        if (args.updateButtonClick == UPDATE_PROCESS){
            binding.btnAddLesson.text = getString(R.string.update)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lessonName.setText(args.lessonObject.lessonName)
        binding.lessonMideTermExam.setText(args.lessonObject.lessonMidTermExam)
        binding.lessonFinalExam.setText(args.lessonObject.lessonFinalExam)

        binding.btnAddLesson.setOnClickListener {
            view.let {
                if (args.updateButtonClick == UPDATE_PROCESS){
                    updateFirestoreLessonInfo(it)
                    updateDbLessonInfo(it)
                }else{
                    val uuid = UUID.randomUUID()
                    saveFirestoreLessonInfo(it, uuid.toString())
                    saveDbLessonInfo(it,uuid.toString())
                }
            }
        }
    }

    private fun updateDbLessonInfo(view: View) {
        val lessonName = binding.lessonName.text.toString()
        val lessonMidTermExam = binding.lessonMideTermExam.text.toString()
        val lessonFinalExam = binding.lessonFinalExam.text.toString()

        if (lessonName.isNotEmpty() && lessonMidTermExam.isNotEmpty() && lessonFinalExam.isNotEmpty()) {

            val average = (lessonMidTermExam.toInt() * 0.4) + (lessonFinalExam.toInt() * 0.6)

            val studentFullName = args.studentObject.studentFullName
            val studentId = args.studentObject.studentId

            val lesson = Lesson(
                args.lessonObject.lessonId, studentId, lessonName, lessonMidTermExam,
                lessonFinalExam, average.toString(), studentFullName
            )

            lessonViewModel.update(lesson)
            binding.lessonName.text.clear()
            binding.lessonMideTermExam.text.clear()
            binding.lessonFinalExam.text.clear()

            val goToLessonList = AddLessonFragmentDirections
                .addLessonFragmentToLessonsListFragment(args.studentObject)
            Navigation.findNavController(view).navigate(goToLessonList)

        } else {
            Toast.makeText(context, getString(R.string.all_fields_filled), Toast.LENGTH_LONG).show()
        }
    }

    private fun updateFirestoreLessonInfo(view: View) {
        val studentFullName = args.studentObject.studentFullName
        val studentId = args.studentObject.studentId

        val lessonName = binding.lessonName.text.toString()
        val lessonMidTermExam = binding.lessonMideTermExam.text.toString()
        val lessonFinalExam = binding.lessonFinalExam.text.toString()



        if (lessonName.isNotEmpty() && lessonMidTermExam.isNotEmpty() && lessonFinalExam.isNotEmpty()) {

            val average = (lessonMidTermExam.toInt() * 0.4) + (lessonFinalExam.toInt() * 0.6)

            val lessonInfo = HashMap<String,Any>()
            lessonInfo[FIELD_LESSON_ID] = args.lessonObject.lessonId
            lessonInfo[FIELD_LESSON_STUDENT_ID] = studentId
            lessonInfo[FIELD_LESSON_NAME] = lessonName
            lessonInfo[FIELD_LESSON_MIDTERM_EXAM] = lessonMidTermExam
            lessonInfo[FIELD_LESSON_FINAL_EXAM] = lessonFinalExam
            lessonInfo[FIELD_LESSON_AVERAGE_GRADE] = average.toString()
            lessonInfo[FIELD_LESSON_STUDENT_FULL_NAME] = studentFullName

            FirestoreClass().updateLesson(args.lessonObject.lessonId, view.findFragment(), lessonInfo, view)
        }
    }


    private fun saveDbLessonInfo(view: View, uuid: String) {
        val lessonName = binding.lessonName.text.toString()
        val lessonMidTermExam = binding.lessonMideTermExam.text.toString()
        val lessonFinalExam = binding.lessonFinalExam.text.toString()

        if (lessonName.isNotEmpty() && lessonMidTermExam.isNotEmpty() && lessonFinalExam.isNotEmpty()) {

            val average = (lessonMidTermExam.toInt() * 0.4) + (lessonFinalExam.toInt() * 0.6)

            val studentFullName = args.studentObject.studentFullName
            val studentId = args.studentObject.studentId

            val lesson = Lesson(
                uuid, studentId, lessonName, lessonMidTermExam,
                lessonFinalExam, average.toString(), studentFullName
            )

            lessonViewModel.insert(lesson)
            binding.lessonName.text.clear()
            binding.lessonMideTermExam.text.clear()
            binding.lessonFinalExam.text.clear()

            val goToLessonList = AddLessonFragmentDirections
                .addLessonFragmentToLessonsListFragment(args.studentObject)
            Navigation.findNavController(view).navigate(goToLessonList)

        } else {
            Toast.makeText(context, getString(R.string.all_fields_filled), Toast.LENGTH_LONG).show()
        }
    }

    fun saveFirestoreLessonInfo(view: View, uuid: String) {
        val studentFullName = args.studentObject.studentFullName
        val studentId = args.studentObject.studentId

        val lessonName = binding.lessonName.text.toString()
        val lessonMidTermExam = binding.lessonMideTermExam.text.toString()
        val lessonFinalExam = binding.lessonFinalExam.text.toString()



        if (lessonName.isNotEmpty() && lessonMidTermExam.isNotEmpty() && lessonFinalExam.isNotEmpty()) {

            val average = (lessonMidTermExam.toInt() * 0.4) + (lessonFinalExam.toInt() * 0.6)

            val lessonInfo =
                Lesson(uuid,
                studentId,lessonName, lessonMidTermExam,
                    lessonFinalExam,average.toString(),studentFullName)

            FirestoreClass().addLesson(uuid, view.findFragment(), lessonInfo, view)
        }
    }
}