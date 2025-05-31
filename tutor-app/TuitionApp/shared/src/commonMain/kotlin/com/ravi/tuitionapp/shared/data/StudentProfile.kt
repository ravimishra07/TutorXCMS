package com.ravi.tuitionapp.shared.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.datetime.LocalDate

@Serializable
data class StudentProfile(
    @SerialName("student_id")
    val studentId: String = "",
    
    val name: String = "",
    
    @SerialName("class")
    val studentClass: Int = 0,
    
    val board: String = "",
    val course: String? = null, // Reference to course ID
    val location: String? = null,
    val mob: String = "",
    val email: String = "",
    val stream: String = "",
    val subjects: List<String> = emptyList(),
    
    @SerialName("assigned_tutor")
    val assignedTutor: String = "",
    
    @SerialName("admission_date")
    val admissionDate: LocalDate = LocalDate(2024, 1, 1),
    
    val notes: String = "",
    
    @SerialName("is_active")
    val isActive: Boolean = true,
    
    val progress: Map<String, Map<String, Boolean>> = emptyMap()
)

@Serializable
data class Course(
    val id: String = "",
    val board: Board = Board.CBSE,
    
    @SerialName("class")
    val courseClass: Int = 0,
    
    val syllabus: List<Syllabus> = emptyList()
)

@Serializable
enum class Board {
    CBSE, ICSE, State
}

@Serializable
data class Syllabus(
    val id: String = "",
    val subject: String = "",
    val chapters: List<String> = emptyList()
) 