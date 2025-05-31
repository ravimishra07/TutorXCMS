package com.ravi.tuitionapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ravi.tuitionapp.Home
import com.ravi.tuitionapp.screens.StudentsScreen
import com.ravi.tuitionapp.screens.StudentDetailsScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Students : Screen("students")
    object AddStudent : Screen("add_student")
    object StudentDetails : Screen("student_details/{studentId}") {
        fun createRoute(studentId: String) = "student_details/$studentId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            Home(
                onStudentsClick = {
                    navController.navigate(Screen.Students.route)
                }
            )
        }
        
        composable(Screen.Students.route) {
            StudentsScreen(
                onAddStudent = {
                    navController.navigate(Screen.AddStudent.route)
                },
                onStudentClick = { studentId ->
                    navController.navigate(Screen.StudentDetails.createRoute(studentId))
                }
            )
        }
        
        // TODO: Add other screen composables
        composable(Screen.AddStudent.route) {
            // TODO: Implement AddStudentScreen
        }
        
        composable(Screen.StudentDetails.route) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getString("studentId") ?: ""
            StudentDetailsScreen(
                studentId = studentId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
} 