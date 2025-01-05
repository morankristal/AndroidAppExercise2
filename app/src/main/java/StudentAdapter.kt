package com.example.subscriptiondatabase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
    private val students: MutableList<Student>,
    private val onClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_item, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.bind(student)
    }

    override fun getItemCount(): Int = students.size

    fun addStudent(student: Student) {
        students.add(student)
        notifyItemInserted(students.size - 1)
    }

    fun updateStudent(student: Student) {
        val index = students.indexOfFirst { it.id == student.id }
        if (index != -1) {
            students[index] = student
            notifyItemChanged(index)
        }
    }

    fun deleteStudent(studentId: Int) {
        val index = students.indexOfFirst { it.id == studentId }
        if (index != -1) {
            students.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val studentImage: ImageView = itemView.findViewById(R.id.imageViewStudent)
        private val studentName: TextView = itemView.findViewById(R.id.textViewName)
        private val studentId: TextView = itemView.findViewById(R.id.textViewStudentId)
        private val studentCheckBox: CheckBox = itemView.findViewById(R.id.checkBoxStudent)

        fun bind(student: Student) {
            studentImage.setImageResource(R.drawable.ic_student)
            studentName.text = student.name
            studentId.text = "ID: ${student.id}"

            studentCheckBox.isChecked = student.isChecked

            studentCheckBox.setOnCheckedChangeListener { _, isChecked ->
                student.isChecked = isChecked
                StudentRepository.updateStudent(student)
            }

            itemView.setOnClickListener { onClick(student) }
        }
    }
}
