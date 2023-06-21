@file:Suppress("unused")

package com.petrunnel.memoria

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

object Utils {

    fun switchVisibleOrInvisible(v: View) {
        if (v.isVisible) v.visibility = View.INVISIBLE else v.visibility = View.VISIBLE
    }

    fun switchVisibleOrGone(v: View) {
        if (v.isVisible) v.visibility = View.GONE else v.visibility = View.VISIBLE
    }
}

fun Context.toastLong(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.toastShort(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showAbout(activity: AppCompatActivity? = null) {
    val authorName = getString(R.string.author_name)
    AlertDialog
        .Builder(this)
        .setCancelable(false)
        .setTitle(R.string.dialog_about_title)
        .setMessage(getString(R.string.dialog_about_message, authorName))
        .setPositiveButton(R.string.dialog_positive_button_text) { dialog, _ ->
            dialog.dismiss()
            activity?.let {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    it.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                    it.supportActionBar?.hide()
                }
            }
        }
        .show()

}