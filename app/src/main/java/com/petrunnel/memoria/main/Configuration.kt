package com.petrunnel.memoria.main

import android.graphics.Color

data class Configuration(
    private var cols: Int = 6,
    private var rows: Int = 5,
    var type: Boolean = false,
    var pictureCollection: String = "animal",
    var backgroundColor: Int = Color.parseColor("#001E40"),

    ) {
    fun setType(type: String) {
        this.type = type == "3"
    }

    fun getCols() = cols
    fun getRows() = rows

    fun setFieldSize(size: String) {
        when (size) {
            "3x2" -> {
                rows = 3
                cols = 2
            }
            "3x4" -> {
                rows = 3
                cols = 4
            }
            "4x3" -> {
                rows = 4
                cols = 3
            }
            "5x6" -> {
                rows = 5
                cols = 6
            }
            "6x6" -> {
                rows = 6
                cols = 6
            }
        }
    }
}