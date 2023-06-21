package com.petrunnel.memoria.start

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.petrunnel.memoria.PreferenceHelper
import com.petrunnel.memoria.R
import com.petrunnel.memoria.Utils.switchVisibleOrInvisible
import com.petrunnel.memoria.databinding.StartBinding
import com.petrunnel.memoria.main.MemoriaActivity
import com.petrunnel.memoria.records.RecordsActivity
import com.petrunnel.memoria.settings.SettingsActivity
import com.petrunnel.memoria.showAbout
import com.petrunnel.memoria.toastLong

class MemoriaStart : AppCompatActivity() {
    private val authorName by lazy {  getString(R.string.author_name) }
    private val authorNameMessage by lazy { getString(R.string.dialog_about_message, authorName) }

    private lateinit var binding: StartBinding

    @SuppressLint("ClickableViewAccessibility")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.statusBarColor = ContextCompat.getColor(this, R.color.main_color_blue)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController!!.hide(
                android.view.WindowInsets.Type.statusBars()
            )
        }
        with(binding) {
            root.setOnClickListener { switchVisibleOrInvisible(tvAuthorName.root) }
            btnStart.setOnClickListener { startGame() }
            btnSettings.setOnClickListener { startSettings() }
            btnScore.setOnClickListener { startRecords() }
            btnAbout.setOnClickListener { showAbout() }
            btnExit.setOnClickListener { finish() }
        }
    }

    override fun onResume() {
        super.onResume()
        window.statusBarColor = PreferenceHelper(this).loadBackground()
        hideStatusBar()
    }

    private fun startGame() {
        startActivity(Intent(this, MemoriaActivity::class.java))
        toastLong(authorNameMessage)
    }

    private fun startSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
        toastLong(authorNameMessage)
    }

    private fun startRecords() {
        val intent = Intent(this, RecordsActivity::class.java)
        intent.putExtra("color", PreferenceHelper(this).loadStringColor())
        startActivity(intent)
        toastLong(authorNameMessage)
    }

    @Suppress("DEPRECATION")
    private fun hideStatusBar() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            supportActionBar?.hide()
        }
    }
}