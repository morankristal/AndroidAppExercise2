package com.example.subscriptiondatabase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.subscriptiondatabase.MainActivity.Companion.EDIT_STUDENT_REQUEST_CODE

class StudentDetailsActivity : AppCompatActivity() {

    private lateinit var studentImage: ImageView
    private lateinit var studentName: TextView
    private lateinit var studentIdText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_details)

        studentImage = findViewById(R.id.imageViewStudent)
        studentName = findViewById(R.id.textViewName)
        studentIdText = findViewById(R.id.textViewStudentId)

        val studentId = intent.getIntExtra("student_id", -1)

        val student = StudentRepository.getStudentById(studentId)

        val btnReturnToList: Button = findViewById(R.id.btnReturnToList)
        btnReturnToList.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        val btnEditStudent: Button = findViewById(R.id.btnEditStudent)
        btnEditStudent.setOnClickListener {
            val intent = Intent(this, EditStudentActivity::class.java)
            intent.putExtra("student_id", student?.id)
            startActivityForResult(intent, EDIT_STUDENT_REQUEST_CODE)
        }

        student?.let {
            studentImage.setImageResource(R.drawable.ic_student)
            studentName.text = it.name
            studentIdText.text = "ID: ${it.id}"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EDIT_STUDENT_REQUEST_CODE && resultCode == RESULT_OK) {
            // Handle deletion
            val deletedStudentId = data?.getIntExtra("deleted_student_id", -1)
            if (deletedStudentId != null && deletedStudentId != -1) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
                return
            }

            val updatedStudentId = data?.getIntExtra("student_id", -1)
            val updatedStudent = StudentRepository.getStudentById(updatedStudentId ?: -1)

            updatedStudent?.let {
                studentName.text = it.name
                studentIdText.text = "ID: ${it.id}"
                studentImage.setImageResource(R.drawable.ic_student)
            }
        }
    }
}

