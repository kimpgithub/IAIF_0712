package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HistoryScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            CenteredTopAppBar(
                title = "History",
                onHistoryClick = { navController.popBackStack() } // Go back to previous screen
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD4F1BE)) // 전체 배경을 파스텔그린으로 설정
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Sample history cards
            repeat(10) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF7CB342)), // 중간색으로 설정
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "History Item $it",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
