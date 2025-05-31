package com.ravi.tuitionapp.data

import com.google.gson.annotations.SerializedName

data class TutorProfile(
    @SerializedName("tutor_id")
    val tutorId: String = "",
    
    val name: String = "",
    val location: String = "",
    val courses: List<String> = emptyList(), // Array of course references
    
    @SerializedName("subjects_taught")
    val subjectsTaught: List<String> = emptyList(),
    
    @SerializedName("fee_range")
    val feeRange: String = "",
    
    val qualification: String = "",
    
    @SerializedName("student_ids")
    val studentIds: List<String> = emptyList(), // Array of student references
    
    @SerializedName("is_active")
    val isActive: Boolean = true,
    
    @SerializedName("is_verified")
    val isVerified: Boolean = false
) 