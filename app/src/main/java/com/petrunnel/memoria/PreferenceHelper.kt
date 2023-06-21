package com.petrunnel.memoria

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.preference.PreferenceManager

class PreferenceHelper(context: Context) {
    private val settings: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun loadCollection() = settings.getString("PictureCollection", "animal") ?: "animal"
    fun loadBackground() = Color.parseColor(settings.getString("BackgroundColor", "#7E57C2"))
    fun loadStringColor() = settings.getString("BackgroundColor", "#7E57C2")
    fun loadSize() = settings.getString("FieldSize", "4x4") ?: "4x4"
    fun loadType() = settings.getString("GameType", "2") ?: "2"
}