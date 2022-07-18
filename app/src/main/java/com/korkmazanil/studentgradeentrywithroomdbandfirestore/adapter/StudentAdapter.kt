package com.korkmazanil.studentgradeentrywithroomdbandfirestore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.R
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Student
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.view.fragment.StudentsListFragmentDirections

class StudentAdapter(
    private val studentList : List<Student>,
    ): RecyclerView.Adapter<StudentAdapter.StudentViewHolder>(){

    class StudentViewHolder(var view : View) : RecyclerView.ViewHolder(view) {
        var studentFullName : TextView = view.findViewById(R.id.textViewStudentFullName)
        var studentDepartment : TextView = view.findViewById(R.id.textViewStudentDepartment)
        var date : TextView = view.findViewById(R.id.textViewDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.students_list_row,parent,false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {

        holder.studentFullName.text = studentList[position].studentFullName
        holder.studentDepartment.text = studentList[position].studentDepartment
        holder.date.text = studentList[position].studentRegisteredTime

        holder.view.setOnClickListener {
            val student = Student(studentList[position].studentId,
                studentList[position].studentFullName,
                studentList[position].studentDepartment,
                studentList[position].studentRegisteredTime
            )

            val action = StudentsListFragmentDirections.toLessonsListFragment(student)
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return studentList.size
    }
}