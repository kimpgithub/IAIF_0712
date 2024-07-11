package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD4F1BE)) // 전체 배경을 파스텔그린으로 설정
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Sample history cards with dummy data
            item {
                HistoryCard(
                    date = "2024-07-12",
                    time = "12:34 PM",
                    summary = "오늘은 날씨가 정말 좋았어. 점심으로 뭐 먹을지 이야기했어."
                )
            }
            item {
                HistoryCard(
                    date = "2024-07-11",
                    time = "09:21 AM",
                    summary = "새로운 프로젝트를 시작했어. 목표와 계획에 대해 논의했어."
                )
            }
            item {
                HistoryCard(
                    date = "2024-07-10",
                    time = "03:45 PM",
                    summary = "오랜만에 친구를 만났어. 함께 저녁을 먹고 영화도 봤어."
                )
            }
            item {
                HistoryCard(
                    date = "2024-07-09",
                    time = "07:15 AM",
                    summary = "아침 일찍 일어나서 산책을 했어. 상쾌한 기분이었어."
                )
            }
            item {
                HistoryCard(
                    date = "2024-07-08",
                    time = "10:50 AM",
                    summary = "팀 회의가 있었어. 프로젝트 진행 상황을 점검했어."
                )
            }
        }
    }
}

@Composable
fun HistoryCard(date: String, time: String, summary: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF7CB342)), // 중간색으로 설정
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(8.dp, RoundedCornerShape(8.dp)) // 입체감 있는 효과
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = date,
                        color = Color.White,
                        fontSize = 20.sp, // 날짜를 굵고 크게
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = time,
                        color = Color.White,
                        fontSize = 14.sp // 시간은 작은 흰색 글자
                    )
                }
                IconButton(onClick = { /* 상세보기 */ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = Color.White // 점 3개 아이콘 추가
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = summary,
                color = Color.White
            )
        }
    }
}