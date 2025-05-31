package com.ravi.tuitionapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ravi.tuitionapp.data.StudentProfile
import com.ravi.tuitionapp.data.Course
import com.ravi.tuitionapp.data.Syllabus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailsScreen(
    studentId: String,
    onBackClick: () -> Unit = {}
) {
    // TODO: Replace with actual data from repository
    val student = remember {
        StudentProfile(
            studentId = studentId,
            name = "John Doe",
            studentClass = 10,
            board = "CBSE",
            location = "Indira Nagar",
            mob = "9876543210",
            email = "john@example.com",
            stream = "Science",
            subjects = listOf("Physics", "Chemistry", "Mathematics"),
            admissionDate = java.util.Date(),
            progress = mapOf(
                "Physics" to mapOf(
                    "Chapter 1" to true,
                    "Chapter 2" to false,
                    "Chapter 3" to true
                ),
                "Chemistry" to mapOf(
                    "Chapter 1" to true,
                    "Chapter 2" to true,
                    "Chapter 3" to false
                )
            )
        )
    }

    val course = remember {
        Course(
            id = "1",
            board = com.ravi.tuitionapp.data.Board.CBSE,
            courseClass = 10,
            syllabus = listOf(
                Syllabus(
                    id = "1",
                    subject = "Physics",
                    chapters = listOf("Chapter 1", "Chapter 2", "Chapter 3")
                ),
                Syllabus(
                    id = "2",
                    subject = "Chemistry",
                    chapters = listOf("Chapter 1", "Chapter 2", "Chapter 3")
                )
            )
        )
    }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Syllabus", "Progress")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(student.name, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Student Info Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Class ${student.studentClass} - ${student.board}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Stream: ${student.stream}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Contact: ${student.mob}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Tabs
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            // Tab Content
            when (selectedTab) {
                0 -> SyllabusTab(course.syllabus)
                1 -> ProgressTab(student.progress, course.syllabus)
            }
        }
    }
}

@Composable
fun SyllabusTab(syllabus: List<Syllabus>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        syllabus.forEach { subject ->
            Text(
                text = subject.subject,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            subject.chapters.forEach { chapter ->
                Text(
                    text = "• $chapter",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                )
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@Composable
fun ProgressTab(
    progress: Map<String, Map<String, Boolean>>,
    syllabus: List<Syllabus>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        syllabus.forEach { subject ->
            Text(
                text = subject.subject,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            subject.chapters.forEach { chapter ->
                val isCompleted = progress[subject.subject]?.get(chapter) ?: false
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = chapter,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = if (isCompleted) "✓" else "○",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isCompleted) MaterialTheme.colorScheme.primary 
                               else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudentDetailsScreenPreview() {
    MaterialTheme {
        StudentDetailsScreen(studentId = "1")
    }
} 