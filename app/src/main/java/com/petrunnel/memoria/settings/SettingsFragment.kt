package com.petrunnel.memoria.settings

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceFragmentCompat
import com.petrunnel.memoria.R

class SettingsFragment : PreferenceFragmentCompat(), OnPreferenceChangeListener {
    private var collection: ListPreference? = null
    private var color: ListPreference? = null

    private val pictValue: Array<CharSequence> by lazy { collection!!.entries }
    private val colValue: Array<CharSequence> by lazy { color!!.entries }
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
        collection = findPreference("PictureCollection")
        color = findPreference("BackgroundColor")

        collection?.onPreferenceChangeListener = this
        color?.onPreferenceChangeListener = this

        collection?.summary = collection?.entry
        color?.summary = color?.entry
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
            return true
        }

        preference.summary = newValue as CharSequence
        return true
    }


}