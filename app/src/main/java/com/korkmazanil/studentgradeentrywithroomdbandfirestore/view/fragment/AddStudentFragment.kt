package com.korkmazanil.studentgradeentrywithroomdbandfirestore.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.findFragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.FirestoreClass
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.GradeApplication
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.R
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.databinding.FragmentAddStudentBinding
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Student
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.viewmodel.StudentViewModel
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.viewmodel.StudentViewModelFactory
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class AddStudentFragment : Fragment() {

    private lateinit var binding : FragmentAddStudentBinding
    private lateinit var db : FirebaseFirestore
    private lateinit var user : FirebaseAuth

    private val studentViewModel : StudentViewModel by viewModels {
        StudentViewModelFactory((activity?.application as GradeApplication).studentRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
        user = Firebase.auth

    }

        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_student,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddStudent.setOnClickListener {
            val uuid = UUID.randomUUID()
            saveFirestoreStudentInfo(it,uuid)
            saveDbStudentInfo(uuid)
            comeBackStudentListFragment(it)
        }
    }

    private fun comeBackStudentListFragment(it: View) {
        val studentListFragment =
            AddStudentFragmentDirections.toStudentsListFragment()
        Navigation.findNavController(it).navigate(studentListFragment)
    }

    @SuppressLint("SimpleDateFormat")
    fun saveFirestoreStudentInfo(view: View, uuid: UUID) {
        val studentFullName = binding.studentFullName.text.toString()
        val studentDepartment = binding.department.text.toString()
        val mSecondsOfSave = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("MMM dd,yyyy HH:mm")
        val resultDate = Date(mSecondsOfSave)

        if(studentFullName.isNotEmpty() && studentDepartment.isNotEmpty()){
            val studentInfo = Student(
                uuid.toString(),
                studentFullName,
                studentDepartment,
                dateFormat.format(resultDate).toString()
            )
            FirestoreClass().addStudent(uuid,view.findFragment(),studentInfo,view)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun saveDbStudentInfo(uuid: UUID) {
        val studentFullName = binding.studentFullName.text.toString()
        val studentDepartment = binding.department.text.toString()

        val mSecondsOfSave = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("MMM dd,yyyy HH:mm")
        val resultDate = Date(mSecondsOfSave)

        if(studentFullName.isNotEmpty() && studentDepartment.isNotEmpty()){
            val student = Student(
                uuid.toString(),
                studentFullName,
                studentDepartment,
                dateFormat.format(resultDate).toString()
            )

            studentViewModel.insert(student)
            binding.studentFullName.text.clear()
            binding.department.text.clear()

        }else{
            Toast.makeText(context,resources.getString(R.string.all_fields_filled),Toast.LENGTH_LONG).show()
        }

    }
}