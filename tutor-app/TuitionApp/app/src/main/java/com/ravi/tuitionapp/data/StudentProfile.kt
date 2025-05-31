package com.ravi.tuitionapp.data

import com.google.gson.annotations.SerializedName
import java.util.Date

data class StudentProfile(
    val studentId: String = "",
    val name: String = "",
    
    @SerializedName("class")
    val studentClass: Int = 0,
    
    val board: String = "",
    val course: Any? = null, // Firestore reference
    val location: String? = null,
    val mob: String = "",
    val email: String = "",
    val stream: String = "",
    val subjects: List<String> = emptyList(),
    val assignedTutor: String = "",
    val admissionDate: Date = Date(),
    val notes: String = "",
    val isActive: Boolean = true,
    val progress: Map<String, Map<String, Boolean>> = emptyMap()
)

data class Course(
    val id: String = "",
    val board: Board = Board.CBSE,
    
    @SerializedName("class")
    val courseClass: Int = 0,
    
    val syllabus: List<Syllabus> = emptyList()
)

enum class Board {
    CBSE, ICSE, State
}

data class Syllabus(
    val id: String = "",
    val subject: String = "",
    val chapters: List<String> = emptyList()
) 