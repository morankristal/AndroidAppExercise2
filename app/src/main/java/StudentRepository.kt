package com.example.subscriptiondatabase

object StudentRepository {

    private val students = mutableListOf<Student>()

    fun getAllStudents(): List<Student> = students

    fun getStudentById(id: Int): Student? = students.find { it.id == id }

    fun addStudent(student: Student) {
        students.add(student)
    }

    fun updateStudent(student: Student) {
        val index = students.indexOfFirst { it.id == student.id }
        if (index != -1) {
            students[index] = student
        }
    }

    fun removeStudent(studentId: Int) {
        val studentIndex = students.indexOfFirst { it.id == studentId }
        if (studentIndex != -1) {
            students.removeAt(studentIndex)
        }
    }
}

