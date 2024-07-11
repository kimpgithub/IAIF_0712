package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenteredTopAppBar(title: String, onHistoryClick: () -> Unit) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        actions = {
            IconButton(onClick = onHistoryClick) {
                Icon(imageVector = Icons.Default.History, contentDescription = "History")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF388E3C)
        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            CenteredTopAppBar(
                title = "emo-talk",
                onHistoryClick = { navController.navigate("history") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD4F1BE)) // 전체 배경을 파스텔그린으로 설정
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(80.dp)) // 화면 상단과 로고 사이의 여백

            Box(
                modifier = Modifier
                    .size(240.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = "앱 로고",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(80.dp)) // 로고와 사운드 비주얼라이제이션 사이의 여백

            AudioVisualization(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )

            Spacer(modifier = Modifier.weight(1f)) // 남은 공간을 모두 차지하도록 Spacer 추가

            IconButton(
                onClick = { /* Handle recording functionality here */ },
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color.Blue, Color.Cyan)
                        ),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Record",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(30.dp)) // 최하단에서 조금 올라온 위치에 배치
        }
    }
}
