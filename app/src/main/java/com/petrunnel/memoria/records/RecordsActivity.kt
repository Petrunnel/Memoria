package com.petrunnel.memoria.records

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.petrunnel.memoria.PreferenceHelper
import com.petrunnel.memoria.R
import com.petrunnel.memoria.showAbout

class RecordsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(PreferenceHelper(this).loadBackground())
        )
        window.statusBarColor = PreferenceHelper(this).loadBackground()

        val tabBackgroundColor = intent.getStringExtra("color") ?: "#7E57C2"
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.recordsContainer, RecordsFragment(tabBackgroundColor))
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_memu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about -> {
                showAbout()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}