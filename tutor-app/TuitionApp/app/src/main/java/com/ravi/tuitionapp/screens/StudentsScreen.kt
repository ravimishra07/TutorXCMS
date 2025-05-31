package com.ravi.tuitionapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravi.tuitionapp.data.StudentProfile
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentsScreen(
    modifier: Modifier = Modifier,
    onAddStudent: () -> Unit = {},
    onStudentClick: (String) -> Unit = {}
) {
    // TODO: Replace with actual data from repository
    val students = remember {
        listOf(
            StudentProfile(
                studentId = "1",
                name = "John Doe",
                studentClass = 10,
                board = "CBSE",
                location = "Indira Nagar",
                mob = "9876543210",
                email = "john@example.com",
                stream = "Science",
                subjects = listOf("Physics", "Chemistry", "Mathematics"),
                admissionDate = Date()
            ),
            StudentProfile(
                studentId = "2",
                name = "Jane Smith",
                studentClass = 12,
                board = "ICSE",
                location = "Gomti Nagar",
                mob = "9876543211",
                email = "jane@example.com",
                stream = "Commerce",
                subjects = listOf("Accounts", "Business Studies", "Economics"),
                admissionDate = Date()
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Students", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onAddStudent) {
                        Icon(Icons.Default.Add, contentDescription = "Add Student")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (students.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No students found",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(students) { student ->
                    StudentCard(
                        student = student,
                        onClick = { onStudentClick(student.studentId) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentCard(
    student: StudentProfile,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = student.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Class ${student.studentClass} - ${student.board}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Stream: ${student.stream}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Subjects: ${student.subjects.joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudentsScreenPreview() {
    MaterialTheme {
        StudentsScreen()
    }
} 