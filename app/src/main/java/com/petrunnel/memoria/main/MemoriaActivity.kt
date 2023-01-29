package com.petrunnel.memoria.main

import android.content.DialogInterface
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.petrunnel.memoria.records.RecordsFileIO
import com.petrunnel.memoria.databinding.MainBinding

class MemoriaActivity : AppCompatActivity() {
    lateinit var binding: MainBinding
    private var mAdapter: GridAdapter? = null
    private var stepCount: Int = 0
    private val gridSize = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val settings = PreferenceManager.getDefaultSharedPreferences(this)
        val pictureCollection = settings.getString("PictureCollection", "animal")
        val backgroundColor = Color.parseColor(settings.getString("BackgroundColor", "black"))
        stepCount = 0
        mAdapter = GridAdapter(this, gridSize, gridSize, pictureCollection!!)
        binding.apply {
            stepView.text = stepCount.toString()
            timeView.start()
            field.rootView.setBackgroundColor(backgroundColor)
            field.isEnabled = true
            field.numColumns = gridSize
            field.adapter = mAdapter
            field.onItemClickListener =
                OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                    mAdapter?.let {
                        it.checkOpenCells()
                        if (it.openCell(position)) {
                            stepCount++
                            stepView.text = stepCount.toString()
                        }
                        if (it.checkGameOver()) {
                            timeView.stop()
                            showGameOver()
                        }
                    }
                }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) binding.field.numColumns = 9
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) binding.field.numColumns = 6
    }

    private fun showGameOver() {
        val time = binding.timeView.text.toString()
        val ra = RecordsFileIO(this)
        ra.apply {
            addPoint(stepCount)
            addTime(time)
            writeRecords()
        }

        val alertBox = AlertDialog.Builder(this)
        alertBox.apply {
            setTitle("Поздравляем!")
            val textToast = """
            Игра закончена 
            Ходов: $stepCount
            Время: $time
            """.trimIndent()
            setMessage(textToast)
            setNeutralButton("Ok") { _: DialogInterface?, _: Int ->
                finish()
            }
            show()
        }
    }
}