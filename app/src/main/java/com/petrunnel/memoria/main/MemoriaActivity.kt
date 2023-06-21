package com.petrunnel.memoria.main

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.petrunnel.memoria.PreferenceHelper
import com.petrunnel.memoria.R
import com.petrunnel.memoria.Utils.switchVisibleOrGone
import com.petrunnel.memoria.databinding.MainBinding
import com.petrunnel.memoria.records.RecordsFileIO
import com.petrunnel.memoria.showAbout
import com.petrunnel.memoria.toastLong

class MemoriaActivity : AppCompatActivity() {
    private val viewModel: MemoriaViewModel by viewModels()
    private lateinit var binding: MainBinding
    private var mAdapter: GridAdapter? = null

    private val configuration = Configuration()
    private val authorName by lazy {  getString(R.string.author_name) }
    private val authorNameMessage by lazy { getString(R.string.dialog_about_message, authorName) }

    private val itemOnClick: (View, Int, Int) -> Unit = { _, position, _ ->
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

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefHelper = PreferenceHelper(this)
        configuration.pictureCollection = prefHelper.loadCollection()
        configuration.backgroundColor = prefHelper.loadBackground()
        configuration.setFieldSize(prefHelper.loadSize())
        configuration.setType(prefHelper.loadType())
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(configuration.backgroundColor))
        window.statusBarColor = configuration.backgroundColor

        mAdapter = GridAdapter(this, configuration, itemOnClick)

        viewModel.stepCount.observe(this) {
            binding.stepView.text = "Steps: $it"
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
            llInfo.setOnClickListener { switchVisibleOrGone(tvAuthorName.root) }
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
                toastLong(authorNameMessage)
            }
            R.id.about -> {
                showAbout()
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
            setTitle(R.string.dialog_congratulations_title)
            val textToast = getString(R.string.dialog_congratulations_message)
            setMessage(String.format(textToast, viewModel.stepCount.value, time).trimIndent())
            setPositiveButton(R.string.dialog_positive_button_text) { _: DialogInterface?, _: Int ->
                finish()
            }
            show()
        }
    }
}