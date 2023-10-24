package com.example.aitask3.data

import java.lang.Math.pow
import kotlin.math.pow
import kotlin.math.sqrt

data class Circle(
    val centerX: Double = -1.0,
    val centerY: Double = -1.0,
    val state: CircleState = CircleState.EMPTY,
) {

    fun isPointInsideCircle(x: Double, y: Double): Boolean {
        return getDistanceFromPoint(x, y) <= CIRCLE_RADIUS
    }

    private fun getDistanceFromPoint(x: Double, y: Double): Double {
        return sqrt((centerX - x).pow(2.0) + (centerY - y).pow(2.0))
    }
}