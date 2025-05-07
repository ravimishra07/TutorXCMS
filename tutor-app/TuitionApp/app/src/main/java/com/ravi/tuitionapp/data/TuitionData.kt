package com.ravi.tuitionapp.data

data class TuitionData(
    val id: Int,
    val classes: MutableList<String> = mutableListOf(),
    val feesMax: Int? = null,
    val feesMin: Int? = null,
    val location: String? = null,
    val areaTag: String? = null,
    val parentMobile: String? = null,
    val description: String? = null,
    val isFemaleOnly: Boolean = false
)

fun getDummyHomeData() = arrayOf(
    TuitionData(
        id = 1,
        classes = mutableListOf("8th", "9th"),
        feesMin = 2000,
        feesMax = 3000,
        location = "Indira Nagar, Lucknow",
        areaTag = "Indira Nagar",
        parentMobile = "9876543210",
        description = "Looking for experienced tutor for Math and Science.",
        isFemaleOnly = false
    ),
    TuitionData(
        id = 2,
        classes = mutableListOf("6th", "7th"),
        feesMin = 1500,
        feesMax = 2500,
        location = "Gomti Nagar, Lucknow",
        areaTag = "Gomti Nagar",
        parentMobile = "9123456780",
        description = "Home tuition for English and SST, 5 days a week.",
        isFemaleOnly = true
    ),
    TuitionData(
        id = 3,
        classes = mutableListOf("10th"),
        feesMin = 3000,
        feesMax = 4000,
        location = "Aliganj, Lucknow",
        areaTag = "Aliganj",
        parentMobile = "9988776655",
        description = "ICSE board student, focus on board exam preparation.",
        isFemaleOnly = false
    ),
    TuitionData(
        id = 4,
        classes = mutableListOf("5th"),
        feesMin = 1200,
        feesMax = 1600,
        location = "Hazratganj, Lucknow",
        areaTag = "Hazratganj",
        parentMobile = "9001122334",
        description = "Basic tuition needed for all subjects.",
        isFemaleOnly = true
    ),
    TuitionData(
        id = 5,
        classes = mutableListOf("11th", "12th"),
        feesMin = 4000,
        feesMax = 6000,
        location = "Rajajipuram, Lucknow",
        areaTag = "Rajajipuram",
        parentMobile = "9112233445",
        description = "PCM stream, prefer IITian or experienced tutor.",
        isFemaleOnly = false
    )
)
