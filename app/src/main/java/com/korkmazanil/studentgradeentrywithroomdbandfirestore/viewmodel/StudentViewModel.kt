package com.korkmazanil.studentgradeentrywithroomdbandfirestore.viewmodel

import androidx.lifecycle.*
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Student
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.repository.StudentRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class StudentViewModel(private val repository: StudentRepository):ViewModel() {

    val allStudents : LiveData<List<Student>> = repository.allStudents.asLiveData()

    fun insert(student: Student) = viewModelScope.launch {
        repository.insert(student)
    }
}

class StudentViewModelFactory(private val repository: StudentRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return StudentViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}