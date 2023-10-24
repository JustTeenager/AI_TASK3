package com.example.aitask3.ui.circle_field_view

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.aitask3.data.CIRCLE_RADIUS
import com.example.aitask3.data.CircleFieldProcessor
import com.example.aitask3.data.CircleState
import com.example.aitask3.data.PADDING

@Composable
fun CircleFieldComposable(
    modifier: Modifier
) {
    val fieldProcessor by remember {
        mutableStateOf(
            CircleFieldProcessor()
        )
    }

    var totalX by remember {
        mutableStateOf(0)
    }

    var totalY by remember {
        mutableStateOf(0)
    }

    var isEditableField by remember { mutableStateOf(true) }

    LaunchedEffect(totalX, totalY) {
        fieldProcessor.init(
            totalX,
            totalY
        )
    }

    fun DrawScope.calculateCircleCount() {
        val paddedWidth = size.width - 2 * PADDING
        val paddedHeight = size.height - 2 * PADDING

        totalX = (paddedWidth / (2 * CIRCLE_RADIUS + PADDING)).toInt()
        totalY = (paddedHeight / (2 * CIRCLE_RADIUS + PADDING)).toInt()
    }

    Column(
        modifier
            .fillMaxSize()
            .padding(1.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Canvas(modifier = modifier
            .fillMaxHeight(0.8f)
            .pointerInput(true) {
                detectTapGestures {
                    if (isEditableField) {
                        fieldProcessor.moveTappedCircle(it)
                    }
                }
            }) {
            calculateCircleCount()

            val field =
                if (isEditableField) fieldProcessor.circleField.modifiableField else fieldProcessor.circleField.initialField

            field.forEach { circleRow ->
                circleRow.column.forEach {
                    val color = when (it.state) {
                        CircleState.RED -> Color.Red
                        CircleState.BLUE -> Color.Blue
                        CircleState.EMPTY -> Color.Gray
                    }
                    drawCircle(
                        color,
                        CIRCLE_RADIUS,
                        Offset(it.centerX.toFloat(), it.centerY.toFloat())
                    )
                }
            }
        }

        Button(
            onClick = {
                Log.d("tut", "onClick")
                fieldProcessor.moveUnhappyCircles()
            },
            enabled = isEditableField
        ) {
            Text(text = "Преобразовать")
        }

        Switch(
            checked = isEditableField,
            onCheckedChange = {
                isEditableField = it
            }
        )
    }
}