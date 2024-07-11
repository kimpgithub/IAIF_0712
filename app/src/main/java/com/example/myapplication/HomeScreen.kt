package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

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
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isRecording by remember { mutableStateOf(false) }
    var isRecorderInitialized by remember { mutableStateOf(false) }
    val mediaRecorder = remember { MediaRecorder() }
    val filePath = remember { context.externalCacheDir?.absolutePath + "/audiorecord.wav" }

    // Permission handling
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissions.entries.forEach {
                Log.d("Permission", "${it.key} = ${it.value}")
            }
        }
    )

    LaunchedEffect(Unit) {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permissions.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }) {
            // Permissions are already granted
        } else {
            requestPermissionLauncher.launch(permissions)
        }
    }

    fun startRecording() {
        try {
            mediaRecorder.apply {
                reset() // MediaRecorder를 재설정
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4) // WAV 형식으로 설정
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC) // AAC 코덱 사용
                setOutputFile(filePath)
                prepare()
                start()
            }
            isRecording = true
            isRecorderInitialized = true
        } catch (e: IOException) {
            Log.e("AudioRecordTest", "prepare() failed")
        }
    }

    fun stopRecording() {
        if (isRecorderInitialized) {
            try {
                mediaRecorder.apply {
                    stop()
                    release()
                }
                isRecorderInitialized = false
            } catch (e: IllegalStateException) {
                Log.e("AudioRecordTest", "stop() failed")
            }
        }
        isRecording = false
        // Upload the file
        coroutineScope.launch(Dispatchers.IO) {
            uploadAudioFile(File(filePath))
        }
    }

    fun toggleRecording() {
        if (isRecording) {
            stopRecording()
        } else {
            startRecording()
        }
    }

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
                onClick = { toggleRecording() },
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
                    imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = if (isRecording) "Stop Recording" else "Record",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(30.dp)) // 최하단에서 조금 올라온 위치에 배치
        }
    }
}

suspend fun uploadAudioFile(file: File) {
    val client = OkHttpClient()
    val requestBody = file.asRequestBody("audio/wav".toMediaTypeOrNull())
    val body = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("file", file.name, requestBody)
        .build()
    val request = Request.Builder()
        .url("http://112.171.8.64:5000/upload") // Flask 서버의 업로드 엔드포인트 URL로 변경
        .post(body)
        .build()
    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")
        Log.d("Upload", "File uploaded successfully")
    }
}
