package com.ravi.tuitionapp.shared.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class TutorProfile(
    @SerialName("tutor_id")
    val tutorId: String = "",
    
    val name: String = "",
    val location: String = "",
    val courses: List<String> = emptyList(), // Array of course references
    
    @SerialName("subjects_taught")
    val subjectsTaught: List<String> = emptyList(),
    
    @SerialName("fee_range")
    val feeRange: String = "",
    
    val qualification: String = "",
    
    @SerialName("student_ids")
    val studentIds: List<String> = emptyList(), // Array of student references
    
    @SerialName("is_active")
    val isActive: Boolean = true,
    
    @SerialName("is_verified")
    val isVerified: Boolean = false
) 