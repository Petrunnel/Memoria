package com.petrunnel.memoria.start

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.petrunnel.memoria.R
import com.petrunnel.memoria.databinding.StartBinding
import com.petrunnel.memoria.main.MemoriaActivity
import com.petrunnel.memoria.records.RecordsActivity
import com.petrunnel.memoria.settings.SettingsActivity

class MemoriaStart : AppCompatActivity() {
    private lateinit var binding: StartBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.statusBarColor = ContextCompat.getColor(this, R.color.main_color_blue)
        with(binding) {
            btnStart.setOnClickListener { startGame() }
            btnSettings.setOnClickListener { startSettings() }
            btnScore.setOnClickListener { startRecords() }
            btnAbout.setOnClickListener { showAbout() }
            btnExit.setOnClickListener { finish() }
        }
    }

    private fun startGame() {
        startActivity(Intent(this, MemoriaActivity::class.java))
    }

    private fun startSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun startRecords() {
        startActivity(Intent(this, RecordsActivity::class.java))
    }

    private fun showAbout() {
        AlertDialog
            .Builder(this)
            .setCancelable(false)
            .setTitle("Об авторе")
            .setMessage("Разработал \nстудент группы АС573\nКотыло Павел")
            .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}