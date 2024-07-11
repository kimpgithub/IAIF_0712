package com.example.myapplication

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

@Composable
fun AudioVisualization(modifier: Modifier = Modifier) {
    // Simulate audio data
    val audioData = remember { mutableStateListOf<Float>() }

    // Update the audio data every 100 milliseconds
    LaunchedEffect(Unit) {
        while (true) {
            audioData.add(Random.nextFloat())
            if (audioData.size > 100) {
                audioData.removeAt(0)
            }
            kotlinx.coroutines.delay(100)
        }
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val step = if (audioData.isNotEmpty()) width / audioData.size else 0f
        val barWidth = step / 2

        for (index in audioData.indices) {
            val x = index * step
            val barHeight = height * audioData[index]
            val y = height - barHeight

            drawRect(
                color = Color.Blue,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight)
            )
        }
    }
}
