package com.petrunnel.memoria.main

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import com.petrunnel.memoria.databinding.MainBinding
import com.petrunnel.memoria.records.RecordsFileIO

class MemoriaActivity : AppCompatActivity() {
    private val viewModel: MemoriaViewModel by viewModels()
    lateinit var binding: MainBinding
    private var mAdapter: GridAdapter? = null

    private var mRows = 4
    private var mCols = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val settings = PreferenceManager.getDefaultSharedPreferences(this)
        val pictureCollection = settings.getString("PictureCollection", "animal")
        val backgroundColor = Color.parseColor(settings.getString("BackgroundColor", "black"))
        val size = settings.getString("FieldSize", "4x4") ?: "4x4"
        setFieldSize(size)
        mAdapter = if (savedInstanceState != null) {
            val arrPict = savedInstanceState.getStringArrayList("arrPict") ?: ArrayList()
            val arrStatus =
                savedInstanceState.getSerializable("arrStatus") as ArrayList<GridAdapter.Status>
            GridAdapter(this, mCols, mRows, pictureCollection!!, arrPict, arrStatus, itemOnClick)
        } else {
            GridAdapter(this, mCols, mRows, pictureCollection!!, itemClickListener = itemOnClick)
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
            field.layoutManager =
                GridLayoutManager(this@MemoriaActivity, mRows, GridLayoutManager.VERTICAL, false)
            field.adapter = mAdapter
        }
    }

    private val itemOnClick: (View, Int, Int) -> Unit = { view, position, type ->
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

    private fun setFieldSize(size: String) {
        when (size) {
            "2x2" -> {
                mRows = 2
                mCols = 2
            }
            "2x4" -> {
                mRows = 2
                mCols = 4
            }
            "3x2" -> {
                mRows = 3
                mCols = 2
            }
            "3x4" -> {
                mRows = 3
                mCols = 4
            }
            "4x3" -> {
                mRows = 4
                mCols = 3
            }
            "4x4" -> {
                mRows = 4
                mCols = 4
            }
            "4x5" -> {
                mRows = 4
                mCols = 5
            }
            "5x6" -> {
                mRows = 5
                mCols = 6
            }
            "6x6" -> {
                mRows = 6
                mCols = 6
            }
        }
    }
}