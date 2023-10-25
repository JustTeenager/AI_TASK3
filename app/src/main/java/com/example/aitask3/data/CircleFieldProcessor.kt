package com.example.aitask3.data

import androidx.compose.ui.geometry.Offset

class CircleFieldProcessor(startCircleField: CircleField = CircleField()) {

    val circleField = startCircleField

    fun init(totalCirclesX: Int, totalCirclesY: Int) {
        circleField.initFields(totalCirclesX, totalCirclesY)

        for (i in 0 until totalCirclesX) {
            for (j in 0 until totalCirclesY) {
                circleField.addCircleToFields(
                    CIRCLE_RADIUS + PADDING + (i * (PADDING + 2 * CIRCLE_RADIUS).toDouble()),
                    CIRCLE_RADIUS + PADDING + (j * (PADDING + 2 * CIRCLE_RADIUS).toDouble()),
                    CircleState.values().random(),
                    i,
                    j,
                )
            }
        }
    }

    fun moveUnhappyCircles(times: Int = 1) {
        repeat(times) {
            val newField = mutableListOf<CircleColumn>().apply {
                addAll(circleField.modifiableField)
            }

            newField.forEachCircle { circle ->
                moveCircleIfUnhappy(circle)
            }
        }
    }

    fun moveTappedCircle(offset: Offset) {
        val circle = circleField.getTouchedCircle(offset)
        circle?.let {
            moveCircleIfUnhappy(circle)
        }
    }

    private fun moveCircleIfUnhappy(
        circle: Circle,
    ) {
        val newField = mutableListOf<CircleColumn>().apply {
            addAll(circleField.modifiableField)
        }

        val happiness = getCircleHappiness(circle)

        if (happiness < MINIMUM_HAPPINESS_VALUE) {
            val emptyCircle =
                newField
                    .flatMap { it.column }
                    .filter { it.state == CircleState.EMPTY }
                    .random()

            swapElements(emptyCircle, circle)
        }
    }

    private fun getCircleHappiness(circle: Circle): Float {
        if (circle.state != CircleState.EMPTY) {
            var goodNeighbours = 0f

            val neighbours = circleField.getCircleNeighbours(circle)

            neighbours.forEach {
                if (circle.state == it.state) {
                    goodNeighbours++
                }
            }

            return goodNeighbours / neighbours.size
        }
        return 1f
    }

    private fun swapElements(
        emptyCircle: Circle,
        circle: Circle
    ) {
        val circleCoords = circleField.getCirclePositionInField(circle)
        val emptyCircleCoords = circleField.getCirclePositionInField(emptyCircle)

        circleCoords?.let {
            emptyCircleCoords?.let {

                val circleColumn = circleField.modifiableField[circleCoords.first]
                val emptyCircleColumn = circleField.modifiableField[emptyCircleCoords.first]

                if (circleCoords.first != emptyCircleCoords.first) {
                    swapElementsOnDifferentColumns(
                        circleCoords,
                        circleColumn,
                        emptyCircle,
                        emptyCircleCoords,
                        emptyCircleColumn,
                        circle
                    )
                } else {
                    swapElementsOnTheSameColumn(
                        circleColumn,
                        circleCoords,
                        emptyCircle,
                        emptyCircleCoords,
                        circle
                    )
                }
            }
        }
    }

    private fun swapElementsOnDifferentColumns(
        circleCoords: Pair<Int, Int>,
        circleColumn: CircleColumn,
        emptyCircle: Circle,
        emptyCircleCoords: Pair<Int, Int>,
        emptyCircleColumn: CircleColumn,
        circle: Circle
    ) {
        circleField.modifiableField[circleCoords.first] =
            circleColumn.copy(
                column = circleColumn.column
                    .toMutableList()
                    .apply {
                        this[circleCoords.second] =
                            this[circleCoords.second].copy(
                                state = emptyCircle.state
                            )
                    }
            )

        circleField.modifiableField[emptyCircleCoords.first] =
            emptyCircleColumn.copy(
                column = emptyCircleColumn.column
                    .toMutableList()
                    .apply {
                        this[emptyCircleCoords.second] =
                            this[emptyCircleCoords.second].copy(
                                state = circle.state
                            )
                    }
            )
    }

    private fun swapElementsOnTheSameColumn(
        circleColumn: CircleColumn,
        circleCoords: Pair<Int, Int>,
        emptyCircle: Circle,
        emptyCircleCoords: Pair<Int, Int>,
        circle: Circle
    ) {
        val resultColumn = circleColumn.column.toMutableList().apply {
            this[circleCoords.second] = this[circleCoords.second].copy(
                state = emptyCircle.state
            )
            this[emptyCircleCoords.second] = this[emptyCircleCoords.second].copy(
                state = circle.state
            )
        }
        circleField.modifiableField[emptyCircleCoords.first] =
            circleField.modifiableField[emptyCircleCoords.first].copy(
                column = resultColumn
            )
    }
}