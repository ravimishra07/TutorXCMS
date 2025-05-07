package com.ravi.tuitionapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravi.tuitionapp.data.TuitionData
import com.ravi.tuitionapp.data.getDummyHomeData


@Composable
fun MainScreen(paddingValues: PaddingValues) {
    Column(modifier = Modifier.padding(paddingValues)) {
        TutorTopBar()
        TuitionListView(getDummyHomeData())
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorTopBar(
    title: String = "TutorApp",
    subtitle: String = "YourTag",
) {
    Column(modifier = Modifier.padding(horizontal = 12.dp)) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Dropdown")
            }
        }

        Text("Good Morning", fontSize = 14.sp)
        Spacer(Modifier.height(6.dp))

    }


}


@Composable
fun TuitionListView(tuitionList: Array<TuitionData>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        items(tuitionList.size) { index ->
            TuitionCard(tuitionList[index])
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainPreview() {
    TutorTopBar()
}

@Preview
@Composable
private fun MainPreView() {
    TuitionListView(getDummyHomeData())
}


@Composable
fun TuitionCard(tuition: TuitionData) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9FB)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = tuition.location ?: "Unknown Location",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Classes: ${tuition.classes.joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )
            if (tuition.feesMin != null && tuition.feesMax != null) {
                Text(
                    text = "Fees: ₹${tuition.feesMin} - ₹${tuition.feesMax}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
            }
            Text(
                text = tuition.description ?: "",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = "Contact: ${tuition.parentMobile ?: "-"}",
                style = MaterialTheme.typography.labelSmall.copy(color = Color.DarkGray)
            )
            if (tuition.isFemaleOnly) {
                Text(
                    text = "Female Tutor Preferred",
                    color = Color(0xFFD81B60),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}

