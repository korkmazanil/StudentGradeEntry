package com.korkmazanil.studentgradeentrywithroomdbandfirestore.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.findFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.FirestoreClass
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.GradeApplication
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.R
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.adapter.StudentAdapter
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.databinding.FragmentStudentsListBinding
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Student
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.viewmodel.StudentViewModel
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.viewmodel.StudentViewModelFactory
import kotlin.collections.ArrayList

class StudentsListFragment : BaseFragment() {

    private var _binding : FragmentStudentsListBinding? = null
    private val binding : FragmentStudentsListBinding get() = _binding!!

    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    private lateinit var studentList : ArrayList<Student>
    private lateinit var adapter : StudentAdapter

    private lateinit var searchStudentList: ArrayList<Student>

            private val studentViewModel: StudentViewModel by viewModels {
        StudentViewModelFactory((activity?.application as GradeApplication).studentRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
        auth = Firebase.auth
        studentList = ArrayList()
        searchStudentList = ArrayList()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_students_list, container, false)
        binding.rvStudentList.layoutManager = LinearLayoutManager(context)
        binding.rvStudentList.setHasFixedSize(true)

        binding.studentSearchView.let {
            it.clearFocus()
            it.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    if (p0 != null){
                        searchStudentName(p0)
                    }
                    return true
                }
            })
        }

        return binding.root
    }

    private fun searchStudentName(search: String) {

        val searchQuery = "%$search%"
        studentViewModel.searchStudentName(searchQuery).observe(viewLifecycleOwner, Observer { list->

            list.let {
                val adapter = StudentAdapter(it)
                adapter.setData(it)
                binding.rvStudentList.adapter = adapter
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddStudent.setOnClickListener {
            val fabAddLesson =
                StudentsListFragmentDirections.toAddStudentFragment()

            Navigation.findNavController(requireView()).navigate(fabAddLesson)
        }

        binding.fabFilter.setOnClickListener {
            if (binding.studentSearchView.visibility == View.GONE){
                binding.studentSearchView.visibility = View.VISIBLE
            }else{
                binding.studentSearchView.visibility = View.GONE
            }
        }
        observeLiveData(view)
    }

    private fun observeLiveData(view: View) {
        studentViewModel.allStudents.observe(viewLifecycleOwner, Observer { students ->
            students?.let { student ->
                    if (student.isNotEmpty()){
                        val adapter = StudentAdapter(student)
                        binding.rvStudentList.adapter = adapter
                    }else{
                        binding.fabGetStudents.setOnClickListener {
                            FirestoreClass().getStudents(view.findFragment())
                        }
                    }
            }
        })
    }

    fun getStudents(studentList: ArrayList<Student>) {

                studentList.forEach {
                    val studentId = it.studentId
                    val studentName = it.studentFullName
                    val studentDepartment = it.studentDepartment
                    val studentRegisteredTime = it.studentRegisteredTime

                    val student = Student(studentId,studentName,studentDepartment,studentRegisteredTime)
                    studentViewModel.insert(student)
                }
            val adapter = StudentAdapter(studentList)
            binding.rvStudentList.adapter = adapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



