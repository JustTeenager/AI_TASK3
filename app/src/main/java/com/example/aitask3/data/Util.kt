package com.example.aitask3.data

fun List<CircleColumn>.forEachCircle(action: (Circle) -> Unit) {
    forEach { column ->
        column.column.forEach { circle ->
            action(circle)
        }
    }
}