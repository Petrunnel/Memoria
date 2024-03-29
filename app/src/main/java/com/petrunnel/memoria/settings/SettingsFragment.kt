package com.petrunnel.memoria.settings

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceFragmentCompat
import com.petrunnel.memoria.PreferenceHelper
import com.petrunnel.memoria.R

class SettingsFragment : PreferenceFragmentCompat(), OnPreferenceChangeListener {
    private var collection: ListPreference? = null
    private var color: ListPreference? = null
    private var size: ListPreference? = null
    private var type: ListPreference? = null

    private val pictValue: Array<CharSequence> by lazy { collection!!.entries }
    private val colValue: Array<CharSequence> by lazy { color!!.entries }
    private val sizeValue: Array<CharSequence> by lazy { size!!.entries }
    private val typeValue: Array<CharSequence> by lazy { type!!.entries }
    private val preferenceHelper by lazy { PreferenceHelper(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBarAndStatusBarBackground(preferenceHelper.loadBackground())
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
        collection = findPreference("PictureCollection")
        color = findPreference("BackgroundColor")
        size = findPreference("FieldSize")
        type = findPreference("GameType")

        collection?.onPreferenceChangeListener = this
        color?.onPreferenceChangeListener = this
        size?.onPreferenceChangeListener = this
        type?.onPreferenceChangeListener = this

        collection?.summary = collection?.entry
        color?.summary = color?.entry
        size?.summary = size?.entry
        type?.summary = type?.entry
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        val key = preference.key

        if (key == "PictureCollection") {
            val i = (preference as ListPreference).findIndexOfValue(newValue.toString())
            preference.setSummary(pictValue[i])
            return true
        }

        if (key == "BackgroundColor") {
            val i = (preference as ListPreference).findIndexOfValue(newValue.toString())
            preference.setSummary(colValue[i])
            setActionBarAndStatusBarBackground(Color.parseColor(newValue as String))
            return true
        }

        if (key == "FieldSize") {
            val i = (preference as ListPreference).findIndexOfValue(newValue.toString())
            preference.setSummary(sizeValue[i])
            return true
        }

        if (key == "GameType") {
            val i = (preference as ListPreference).findIndexOfValue(newValue.toString())
            preference.setSummary(typeValue[i])
            return true
        }

        preference.summary = newValue as CharSequence
        return true
    }
    private fun setActionBarAndStatusBarBackground(color: Int) {
        (activity as SettingsActivity).supportActionBar?.setBackgroundDrawable(
            ColorDrawable(color)
        )
        (activity as SettingsActivity).window.statusBarColor = color
    }
}