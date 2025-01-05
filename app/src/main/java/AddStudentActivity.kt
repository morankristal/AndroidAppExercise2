package com.example.subscriptiondatabase
import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddStudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        val etStudentId: EditText = findViewById(R.id.editTextStudentId)
        val etStudentName: EditText = findViewById(R.id.editTextName)
        val btnSave: Button = findViewById(R.id.btnSave)

        btnSave.setOnClickListener {
            val studentIdText = etStudentId.text.toString()
            val studentName = etStudentName.text.toString()

            if (studentIdText.isNotBlank() && studentName.isNotBlank()) {
                val studentId = studentIdText.toIntOrNull()

                if (studentId != null) {
                    val existingStudent = StudentRepository.getStudentById(studentId)
                    if (existingStudent != null) {
                        Toast.makeText(this, "ID already exists. Please choose a different ID.", Toast.LENGTH_SHORT).show()
                    } else {
                        val resultIntent = intent
                        resultIntent.putExtra("student_id", studentId)
                        resultIntent.putExtra("student_name", studentName)
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                } else {
                    Toast.makeText(this, "Please enter a valid student ID", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "ID and Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
