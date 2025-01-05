package com.example.subscriptiondatabase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var studentAdapter: StudentAdapter
    private lateinit var recyclerView: RecyclerView

    companion object {
        const val ADD_STUDENT_REQUEST_CODE = 1
        const val EDIT_STUDENT_REQUEST_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewStudents)
        recyclerView.layoutManager = LinearLayoutManager(this)

        studentAdapter = StudentAdapter(StudentRepository.getAllStudents().toMutableList()) { student ->
            val intent = Intent(this, StudentDetailsActivity::class.java)
            intent.putExtra("student_id", student.id)
            startActivityForResult(intent, EDIT_STUDENT_REQUEST_CODE)
        }

        recyclerView.adapter = studentAdapter

        val btnAddStudent: Button = findViewById(R.id.btnAddStudent)
        btnAddStudent.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivityForResult(intent, ADD_STUDENT_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            ADD_STUDENT_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    val studentId = data?.getIntExtra("student_id", -1) ?: -1
                    val name = data?.getStringExtra("student_name") ?: return

                    if (studentId != -1) {
                        val existingStudent = StudentRepository.getStudentById(studentId)
                        if (existingStudent != null) {
                            Toast.makeText(this, "ID already exists. Please choose a different ID.", Toast.LENGTH_SHORT).show()
                        } else {
                            val newStudent = Student(id = studentId, name = name)
                            StudentRepository.addStudent(newStudent)
                            studentAdapter.addStudent(newStudent)
                        }
                    }
                }
            }

            EDIT_STUDENT_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    val studentId = data?.getIntExtra("student_id", -1) ?: -1
                    val updatedName = data?.getStringExtra("student_name")
                    val isChecked = data?.getBooleanExtra("student_is_checked", false) ?: false

                    if (updatedName != null && studentId != -1) {
                        val existingStudentWithNewId = StudentRepository.getStudentById(studentId)

                        val isSameId = existingStudentWithNewId != null && existingStudentWithNewId.id == studentId
                        if (existingStudentWithNewId != null && !isSameId) {
                            Toast.makeText(this, "ID already exists. Please choose a different ID.", Toast.LENGTH_SHORT).show()
                        } else {
                            val student = StudentRepository.getStudentById(studentId)
                            if (student != null) {
                                student.name = updatedName
                                student.isChecked = isChecked
                                studentAdapter.notifyDataSetChanged()
                            }
                        }
                    }

                    val deletedStudentId = data?.getIntExtra("deleted_student_id", -1)
                    if (deletedStudentId != null && deletedStudentId != -1) {
                        studentAdapter.deleteStudent(deletedStudentId)
                    }
                }
            }
        }
    }
}
