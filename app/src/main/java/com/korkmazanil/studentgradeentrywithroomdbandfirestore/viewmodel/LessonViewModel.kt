package com.korkmazanil.studentgradeentrywithroomdbandfirestore.viewmodel

import androidx.lifecycle.*
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Lesson
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.repository.LessonRepository
import kotlinx.coroutines.launch

class LessonViewModel(private val repository: LessonRepository) : ViewModel() {

    val allLessons : LiveData<List<Lesson>> = repository.allLessons.asLiveData()
    val studentWithLessons : LiveData<List<Lesson>> = repository.studentWithLessons.asLiveData()

    fun insert(lesson: Lesson) = viewModelScope.launch {
        repository.insert(lesson)
    }

    fun update(lesson: Lesson) = viewModelScope.launch {
        repository.update(lesson)
    }

    fun delete(lesson: Lesson) = viewModelScope.launch {
        repository.delete(lesson)
    }

    fun deleteSelectLesson(lesson: Lesson) = viewModelScope.launch {
        repository.deleteLesson(lesson)
    }
}

class LessonViewModelFactory(private val repository: LessonRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LessonViewModel::class.java)){

            @Suppress("UNCHECKED_CAST")
            return LessonViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}