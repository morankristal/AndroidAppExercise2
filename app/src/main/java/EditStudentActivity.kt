package com.example.subscriptiondatabase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditStudentActivity : AppCompatActivity() {

    private lateinit var studentIdEditText: EditText
    private lateinit var studentNameEditText: EditText
    private lateinit var studentPic: ImageView
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_student)

        val studentId = intent.getIntExtra("student_id", -1)
        if (studentId == -1) {
            Toast.makeText(this, "Invalid student ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val student = StudentRepository.getStudentById(studentId)
        if (student == null) {
            Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        studentIdEditText = findViewById(R.id.editTextId)
        studentNameEditText = findViewById(R.id.editTextName)
        studentPic = findViewById(R.id.imageViewStudent)
        saveButton = findViewById(R.id.btnSave)
        deleteButton = findViewById(R.id.btnDelete)

        studentIdEditText.setText(student.id.toString())
        studentNameEditText.setText(student.name)

        saveButton.setOnClickListener {
            val newId = studentIdEditText.text.toString().trim()
            val newName = studentNameEditText.text.toString().trim()

            if (newId.isNotEmpty() && newName.isNotEmpty()) {
                if (newId != student.id.toString()) {
                    val existingStudent = StudentRepository.getStudentById(newId.toInt())

                    if (existingStudent != null) {
                        Toast.makeText(this, "This ID already exists. Please choose a different ID.", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    student.id = newId.toInt()
                    student.name = newName

                    StudentRepository.updateStudent(student)

                    val resultIntent = Intent().apply {
                        putExtra("student_id", student.id)
                        putExtra("student_name", newName)
                    }
                    setResult(RESULT_OK, resultIntent)

                    Toast.makeText(this, "Student updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    student.name = newName
                    StudentRepository.updateStudent(student)

                    val resultIntent = Intent().apply {
                        putExtra("student_id", student.id)
                        putExtra("student_name", newName)
                    }
                    setResult(RESULT_OK, resultIntent)

                    Toast.makeText(this, "Student name updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "ID and Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }


        deleteButton.setOnClickListener {
            StudentRepository.removeStudent(studentId)

            val resultIntent = Intent().apply {
                putExtra("deleted_student_id", student.id)
            }
            setResult(RESULT_OK, resultIntent)

            Toast.makeText(this, "${student.name} deleted successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
