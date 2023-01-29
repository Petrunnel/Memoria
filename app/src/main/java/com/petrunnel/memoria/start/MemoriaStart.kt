package com.petrunnel.memoria.start

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.petrunnel.memoria.main.MemoriaActivity
import com.petrunnel.memoria.records.RecordsActivity
import com.petrunnel.memoria.settings.SettingsActivity
import com.petrunnel.memoria.databinding.StartBinding

class MemoriaStart : AppCompatActivity() {
    private lateinit var binding: StartBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            btnStart.setOnClickListener { startGame() }
            btnSettings.setOnClickListener { startSettings() }
            btnScore.setOnClickListener { startRecords() }
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
}