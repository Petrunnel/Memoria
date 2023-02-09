package com.petrunnel.memoria.main

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import com.petrunnel.memoria.R
import com.petrunnel.memoria.databinding.MainBinding
import com.petrunnel.memoria.records.RecordsFileIO

class MemoriaActivity : AppCompatActivity() {
    private val viewModel: MemoriaViewModel by viewModels()
    private lateinit var binding: MainBinding
    private var mAdapter: GridAdapter? = null

    private val configuration = Configuration()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val settings = PreferenceManager.getDefaultSharedPreferences(this)
        configuration.pictureCollection =
            settings.getString("PictureCollection", "animal") ?: "animal"
        configuration.backgroundColor =
            Color.parseColor(settings.getString("BackgroundColor", "white"))
        configuration.setFieldSize(settings.getString("FieldSize", "5x6") ?: "5x6")
        configuration.setType(settings.getString("GameType", "2") ?: "2")

        mAdapter = GridAdapter(this, configuration, itemClickListener = itemOnClick)

        viewModel.stepCount.observe(this) {
            binding.stepView.text = it.toString()
        }
        viewModel.timeText.observe(this) {
            binding.timeView.text = it
        }

        with(binding) {
            field.itemAnimator = null
            field.rootView.setBackgroundColor(configuration.backgroundColor)
            field.isEnabled = true
            field.layoutManager =
                GridLayoutManager(
                    this@MemoriaActivity,
                    configuration.getCols(),
                    GridLayoutManager.VERTICAL,
                    false
                )
            field.adapter = mAdapter
        }
    }

    private val itemOnClick: (View, Int, Int) -> Unit = { view, position, type ->
        if (mAdapter?.getIsClickAllowed() == true) {
            mAdapter?.let {
                it.checkOpenCells()
                if (it.openCell(position)) {
                    viewModel.incStepCount()
                }
                if (it.checkGameOver()) {
                    viewModel.stopwatch.reset()
                    showGameOver()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.stopwatch.resume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopwatch.pause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.memoria_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                viewModel.refresh()
                mAdapter?.fieldInit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showGameOver() {
        val time = binding.timeView.text.toString()
        val ra = RecordsFileIO(this)
        with(ra) {
            addPoint(viewModel.stepCount.value ?: 0)
            addTime(time)
            writeRecords()
        }

        val alertBox = AlertDialog.Builder(this)
        with(alertBox) {
            setCancelable(false)
            setTitle("Поздравляем!")
            val textToast = """
            Игра закончена 
            Ходов: ${viewModel.stepCount.value}
            Время: $time
            """.trimIndent()
            setMessage(textToast)
            setPositiveButton("Ok") { _: DialogInterface?, _: Int ->
                finish()
            }
            show()
        }
    }
}