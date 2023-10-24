package com.example.aitask3.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset

class CircleField {

    val initialField = mutableStateListOf<CircleColumn>()
    val modifiableField = mutableStateListOf<CircleColumn>()

    fun initFields(x: Int, y: Int) {
        if (initialField.isEmpty()) {
            initialField.addAll(
                MutableList(x) { CircleColumn(MutableList(y) { Circle() }) }
            )
            modifiableField.addAll(
                MutableList(x) { CircleColumn(MutableList(y) { Circle() }) }
            )
        }
    }

    fun addCircleToFields(
        x: Double,
        y: Double,
        state: CircleState,
        rowIndex: Int,
        columnIndex: Int
    ) {
        addCircleToField(x, y, state, rowIndex, columnIndex, initialField)
        addCircleToField(x, y, state, rowIndex, columnIndex, modifiableField)
    }

    fun getTouchedCircle(offset: Offset): Circle? {
        var touchedCircle: Circle? = null
        modifiableField.forEachCircle { circle ->
            if (circle.isPointInsideCircle(offset.x.toDouble(), offset.y.toDouble()))
                touchedCircle = circle
        }
        return touchedCircle
    }

    fun getCirclePositionInField(circle: Circle?): Pair<Int, Int>? {
        var rowIndex = -1
        var columnIndex = -1
        modifiableField.forEachIndexed { index, circleRow ->
            if (circleRow.column.contains(circle)) {
                rowIndex = index
                columnIndex = circleRow.column.indexOf(circle)
            }
        }
        return if (rowIndex == -1 || columnIndex == -1) null else rowIndex to columnIndex
    }

    fun getCircleNeighbours(circle: Circle): List<Circle> {

        val circlePosition = getCirclePositionInField(circle)

        val neighbours = mutableListOf<Circle>()

        circlePosition?.let {
            //Левый
            if (circlePosition.first > 0) {
                neighbours.add(
                    getCircleByPosition(
                        circlePosition.first - 1,
                        circlePosition.second
                    )
                )
            }
            // Правый
            if (circlePosition.first < modifiableField.size - 1) {
                neighbours.add(
                    getCircleByPosition(
                        circlePosition.first + 1,
                        circlePosition.second
                    )
                )
            }

            // Верхний
            if (circlePosition.second > 0) {
                neighbours.add(
                    getCircleByPosition(
                        circlePosition.first,
                        circlePosition.second - 1
                    )
                )
            }
            // Нижний
            if (circlePosition.second < modifiableField[0].column.size - 1) {
                neighbours.add(
                    getCircleByPosition(
                        circlePosition.first,
                        circlePosition.second + 1
                    )
                )
            }

            // Левый верхний
            if (circlePosition.first > 0 && circlePosition.second > 0) {
                neighbours.add(
                    getCircleByPosition(
                        circlePosition.first - 1,
                        circlePosition.second - 1
                    )
                )
            }

            // Правый верхний
            if (circlePosition.first < modifiableField.size - 1 && circlePosition.second > 0) {
                neighbours.add(
                    getCircleByPosition(
                        circlePosition.first + 1,
                        circlePosition.second - 1
                    )
                )
            }

            // Левый нижний
            if (circlePosition.first > 0 && circlePosition.second < modifiableField[0].column.size - 1) {
                neighbours.add(
                    getCircleByPosition(
                        circlePosition.first - 1,
                        circlePosition.second + 1
                    )
                )
            }

            // Правый нижний
            if (circlePosition.first < modifiableField.size - 1 && circlePosition.second < modifiableField[0].column.size - 1) {
                neighbours.add(
                    getCircleByPosition(
                        circlePosition.first + 1,
                        circlePosition.second + 1
                    )
                )
            }


        }

        return neighbours
    }

    private fun addCircleToField(
        x: Double,
        y: Double,
        state: CircleState,
        rowIndex: Int,
        columnIndex: Int,
        field: SnapshotStateList<CircleColumn>
    ) {
        val oldColumn = field[rowIndex].column
        field[rowIndex] = field[rowIndex].copy(
            column = oldColumn.toMutableList().apply {
                this[columnIndex] =
                    Circle(
                        x,
                        y,
                        state
                    )
            }
        )
    }

    private fun getCircleByPosition(rowIndex: Int, columnIndex: Int): Circle {
        return modifiableField[rowIndex].column[columnIndex]
    }

}