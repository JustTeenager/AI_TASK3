package com.example.aitask3.data

fun List<CircleColumn>.forEachCircle(action: (Circle) -> Unit) {
    forEach { row ->
        row.column.forEach { circle ->
            action(circle)
        }
    }
}