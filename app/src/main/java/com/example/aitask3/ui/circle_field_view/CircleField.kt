package com.example.aitask3.ui.circle_field_view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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

    var totalCirclesX by remember {
        mutableStateOf(0)
    }

    var totalCirclesY by remember {
        mutableStateOf(0)
    }

    var isEditableField by remember { mutableStateOf(true) }

    LaunchedEffect(totalCirclesX, totalCirclesY) {
        fieldProcessor.init(
            totalCirclesX,
            totalCirclesY
        )
    }

    fun DrawScope.calculateCircleCount() {
        val rawWidth = size.width - 2 * PADDING
        val rawHeight = size.height - 2 * PADDING

        totalCirclesX = (rawWidth / (2 * CIRCLE_RADIUS + PADDING)).toInt()
        totalCirclesY = (rawHeight / (2 * CIRCLE_RADIUS + PADDING)).toInt()
    }

    fun DrawScope.drawCircles() {
        val field =
            if (isEditableField) fieldProcessor.circleField.modifiableField else fieldProcessor.circleField.initialField

        field.forEach { circleColumn ->
            circleColumn.column.forEach {
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

    Column(
        modifier
            .fillMaxSize()
            .padding(1.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Canvas(modifier = modifier
            .fillMaxHeight(0.75f)
            .pointerInput(true) {
                detectTapGestures {
                    if (isEditableField) {
                        fieldProcessor.moveTappedCircle(it)
                    }
                }
            }) {

            calculateCircleCount()
            drawCircles()
        }

        Button(
            onClick = {
                fieldProcessor.moveUnhappyCircles()
            },
            enabled = isEditableField
        ) {
            Text(text = "Преобразовать")
        }

        Spacer(modifier = Modifier.height(3.dp))

        Button(
            onClick = {
                isEditableField = false
                fieldProcessor.init(totalCirclesX, totalCirclesY)
            },
        ) {
            Text(text = "Сгенерить заново")
        }

        Spacer(modifier = Modifier.height(3.dp))

        Switch(
            checked = isEditableField,
            onCheckedChange = {
                isEditableField = it
            }
        )
    }
}