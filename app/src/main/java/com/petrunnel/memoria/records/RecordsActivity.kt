package com.petrunnel.memoria.records

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.petrunnel.memoria.R

class RecordsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.records_container, RecordsFragment())
            .commit()
    }
}