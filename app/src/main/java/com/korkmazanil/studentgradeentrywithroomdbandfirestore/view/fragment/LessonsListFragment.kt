package com.korkmazanil.studentgradeentrywithroomdbandfirestore.view.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.findFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.FirestoreClass
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.GradeApplication
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.R
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.adapter.LessonAdapter
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.databinding.FragmentLessonsListBinding
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Lesson
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.utils.Constants.UPDATE_PROCESS
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.viewmodel.LessonViewModel
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.viewmodel.LessonViewModelFactory

class LessonsListFragment : BaseFragment(), LessonAdapter.OnItemClickListener {

    private lateinit var binding : FragmentLessonsListBinding

    private val args : LessonsListFragmentArgs by navArgs()
    private lateinit var adapter : LessonAdapter
    private var checkOpenAlertDialog : Boolean = true
    // Observe Process
    lateinit var lessonList : ArrayList<Lesson>

    private val lessonViewModel : LessonViewModel by viewModels {
        LessonViewModelFactory((activity?.application as GradeApplication).lessonRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Observe Process Initialize ArrayList
        lessonList = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_lessons_list,container,false)
        binding.rvLessonList.layoutManager = LinearLayoutManager(context)
        binding.rvLessonList.setHasFixedSize(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dummyLesson = Lesson()

        binding.fabAddLesson.setOnClickListener {
            val addLesson = LessonsListFragmentDirections.toAddLessonFragment(
                    args.studentObject,
                    dummyLesson,
                    ""
                )
            Navigation.findNavController(requireView()).navigate(addLesson)
        }

        binding.fabGetStudentsByLesson.setOnClickListener {
            FirestoreClass().getStudentsByLesson(view.findFragment(),"A")
        }
        observeLiveData(view)
    }

    private fun observeLiveData(view: View) {
        lessonViewModel.studentWithLessons.observe(viewLifecycleOwner, Observer { lessons->
            lessonList = lessons as ArrayList<Lesson>
            lessons.let { lesson->
                if (lesson.isNotEmpty()){
                    val listLesson =
                        lessons.filter { it.studentFullName == args.studentObject.studentFullName}
                    adapter = LessonAdapter(listLesson as ArrayList<Lesson>,this)
                    binding.rvLessonList.adapter = adapter

                }
                else if (lesson.isEmpty() && checkOpenAlertDialog){
                    binding.fabGetLessons.setOnClickListener {
                        FirestoreClass().getLessons(view.findFragment())
                    }
                }
            }
        })
    }

    override fun onDeleteIconClick(position: Int, view: View) {

        val clickedItem = adapter.getLessonsList()[position]
        showAlertDialog(clickedItem,position,adapter.getLessonsList())
    }

    override fun onEditIconClick(position: Int, view: View) {

            val clickedItem = adapter.getLessonsList()[position]
            val goToEditStudent = LessonsListFragmentDirections
                .toAddLessonFragment(args.studentObject,clickedItem,UPDATE_PROCESS)
            Navigation.findNavController(view).navigate(goToEditStudent)
        }

    private fun showAlertDialog(
        clickedItem: Lesson,
        position: Int,
        clickedLessonList: ArrayList<Lesson>
    ) {
        val alertDialog = AlertDialog.Builder(requireContext(),
            R.style.MaterialAlertDialog_Background)
            .create()

        with(alertDialog){
            setIcon(R.drawable.ic_white_color_delete_24)
            setMessage(getString(R.string.alert_delete_message))
            setTitle(clickedItem.lessonName)
            setCancelable(true)
            setCanceledOnTouchOutside(false)
            setButton(AlertDialog.BUTTON_NEGATIVE,
                getString(R.string.alert_button_no)) { _, _ ->
                Toast.makeText(
                    requireContext(),
                    getString(R.string.failed_to_delete),
                    Toast.LENGTH_SHORT).show()
            }

            setButton(
                AlertDialog.BUTTON_POSITIVE,
                getString(R.string.alert_button_yes)) { _, _ ->

                FirestoreClass().deleteLesson(clickedLessonList,position)
                lessonViewModel.deleteSelectLesson(clickedItem)

                checkOpenAlertDialog = false
                adapter = LessonAdapter(clickedLessonList ,this@LessonsListFragment)
                adapter.removeItem(position)
                binding.rvLessonList.adapter = adapter

                val deleteMessage = clickedItem.lessonName+" "+ getString(R.string.lesson_deleted)
                Toast.makeText(requireContext(),deleteMessage,Toast.LENGTH_SHORT).show()
            }
            show()
        }
    }

    fun getLessons(lessonList: ArrayList<Lesson>) {

        lessonList.forEach {
            val lessonId = it.lessonId
            val lessonStudentId = it.lessonStudentId
            val lessonName = it.lessonName
            val lessonMidTermExam = it.lessonMidTermExam
            val lessonFinalExam = it.lessonFinalExam
            val lessonAverageGrade = it.lessonAverageGrade
            val studentFullName = it.studentFullName

            val lesson = Lesson(
                lessonId,lessonStudentId,
                lessonName,lessonMidTermExam,
                lessonFinalExam,lessonAverageGrade,
                studentFullName
            )
            lessonViewModel.insert(lesson)
        }

        val adapter = LessonAdapter(lessonList,this)
        binding.rvLessonList.adapter = adapter
    }

    fun getStudentsByLesson(lessonList: ArrayList<Lesson>) {
        val adapter = LessonAdapter(lessonList,this)
        binding.rvLessonList.adapter = adapter
    }
}

