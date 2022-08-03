package com.korkmazanil.studentgradeentrywithroomdbandfirestore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.R
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Lesson

class LessonAdapter(
    private val lessonList : ArrayList<Lesson>,
    private var listener : OnItemClickListener
    ): RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {

    interface OnItemClickListener {
        fun onDeleteIconClick(position: Int,view: View)
        fun onEditIconClick(position: Int,view: View)
        fun onCardViewClick(position: Int, view: View)
    }


    inner class LessonViewHolder(var view: View) : RecyclerView.ViewHolder(view),
    View.OnClickListener{
        var lessonName : TextView = view.findViewById(R.id.textViewLessonName)
        var lessonMidTermExam : TextView = view.findViewById(R.id.textViewlessonMidTermExam)
        var lessonFinalExam : TextView = view.findViewById(R.id.textViewLessonFinalExam)
        var lessonAverageGrade : TextView = view.findViewById(R.id.textViewLessonAverageGrade)
        var imageViewDelete : ImageView = view.findViewById(R.id.imageViewDelete)
        var imageViewEdit : ImageView = view.findViewById(R.id.imageViewEdit)

        init {
            view.setOnClickListener(this)
            imageViewDelete.setOnClickListener(this)
            imageViewEdit.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            when(v.id){
                imageViewDelete.id -> {
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeleteIconClick(position,v)
                    }
                }
                imageViewEdit.id ->{
                    if (position != RecyclerView.NO_POSITION){
                        listener.onEditIconClick(position,v)
                    }
                }

                view.id ->{
                    if (position != RecyclerView.NO_POSITION){
                        listener.onCardViewClick(position,v)
                    }
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.lessons_list_row, parent, false)
        return LessonViewHolder(view)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
            holder.lessonName.text = lessonList[position].lessonName
            holder.lessonMidTermExam.text = lessonList[position].lessonMidTermExam
            holder.lessonFinalExam.text = lessonList[position].lessonFinalExam
            holder.lessonAverageGrade.text = lessonList[position].lessonAverageGrade
    }

    override fun getItemCount(): Int {
        return lessonList.size
    }

    fun removeItem(position: Int) {
        lessonList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, lessonList.size)
    }

    fun restoreItem(item : Lesson,position: Int){
        lessonList.add(position,item)
        notifyItemInserted(position)
    }

    fun getLessonsList() : ArrayList<Lesson>{
        return lessonList
    }
}