package com.petrunnel.memoria.main

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.petrunnel.memoria.databinding.MainBinding
import com.petrunnel.memoria.records.RecordsFileIO

class MemoriaActivity : AppCompatActivity() {
    private val viewModel: MemoriaViewModel by viewModels()
    lateinit var binding: MainBinding
    private var mAdapter: GridAdapter? = null

    private val gridSize = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val settings = PreferenceManager.getDefaultSharedPreferences(this)
        val pictureCollection = settings.getString("PictureCollection", "animal")
        val backgroundColor = Color.parseColor(settings.getString("BackgroundColor", "black"))
        mAdapter = if (savedInstanceState != null) {
            val arrPict = savedInstanceState.getStringArrayList("arrPict") ?: ArrayList()
            val arrStatus = savedInstanceState.getSerializable("arrStatus") as ArrayList<GridAdapter.Status>
            GridAdapter(this, gridSize, gridSize, pictureCollection!!, arrPict, arrStatus)
        } else {
            GridAdapter(this, gridSize, gridSize, pictureCollection!!)
        }
        viewModel.stepCount.observe(this) {
            binding.stepView.text = it.toString()
        }
        viewModel.timeText.observe(this) {
            binding.timeView.text = it
        }

        binding.apply {
            field.rootView.setBackgroundColor(backgroundColor)
            field.isEnabled = true
            field.numColumns = gridSize
            field.adapter = mAdapter
            field.onItemClickListener =
                OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArrayList("arrPict", mAdapter?.getArrPictCells())
        outState.putSerializable("arrStatus", mAdapter?.getArrStatusCells())
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.stopwatch.resume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopwatch.pause()
    }

    private fun showGameOver() {
        val time = binding.timeView.text.toString()
        val ra = RecordsFileIO(this)
        ra.apply {
            addPoint(viewModel.stepCount.value ?: 0)
            addTime(time)
            writeRecords()
        }

        val alertBox = AlertDialog.Builder(this)
        alertBox.apply {
            setTitle("Поздравляем!")
            val textToast = """
            Игра закончена 
            Ходов: ${viewModel.stepCount.value}
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