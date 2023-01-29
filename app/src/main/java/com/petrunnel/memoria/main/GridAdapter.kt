package com.petrunnel.memoria.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.petrunnel.memoria.R
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class GridAdapter(
    private val mContext: Context,
    private val mCols: Int,
    private val mRows: Int,
    pictCollection: String,
    private val arrPict: ArrayList<String?> = ArrayList(),
    private val arrStatus: ArrayList<Status> = ArrayList()
) :
    BaseAdapter() {

    private val pictureCollection: String = pictCollection
    private val mRes: Resources = mContext.resources

    enum class Status: Serializable {
        CELL_OPEN, CELL_CLOSE, CELL_DELETE
    }
    init {
        if (arrPict.isEmpty()) {
            makePictArray()
            closeAllCells()
        }
    }
    override fun getCount(): Int {
        return mCols * mRows
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: ImageView =
            if (convertView == null) ImageView(mContext) else convertView as ImageView
        when (arrStatus[position]) {
            Status.CELL_OPEN -> {
                @SuppressLint("DiscouragedApi") val drawableId =
                    mRes.getIdentifier(arrPict[position], "drawable", mContext.packageName)
                view.setImageResource(drawableId)
            }
            Status.CELL_CLOSE -> view.setImageResource(R.drawable.close)
            else -> view.setImageResource(R.drawable.none)
        }
        return view
    }

    fun checkOpenCells() {
        val first = arrStatus.indexOf(Status.CELL_OPEN)
        val second = arrStatus.lastIndexOf(Status.CELL_OPEN)
        if (first == second) return
        if (arrPict[first] == arrPict[second]) {
            arrStatus[first] = Status.CELL_DELETE
            arrStatus[second] = Status.CELL_DELETE
        } else {
            arrStatus[first] = Status.CELL_CLOSE
            arrStatus[second] = Status.CELL_CLOSE
        }
    }

    fun openCell(position: Int): Boolean {
        if (arrStatus[position] == Status.CELL_DELETE ||
            arrStatus[position] == Status.CELL_OPEN
        ) return false
        if (arrStatus[position] != Status.CELL_DELETE) arrStatus[position] =
            Status.CELL_OPEN
        notifyDataSetChanged()
        return true
    }
    fun checkGameOver(): Boolean {
        return !arrStatus.contains(Status.CELL_CLOSE)
    }
    fun getArrPictCells() = arrPict
    fun getArrStatusCells() = arrStatus
    private fun makePictArray() {
        arrPict.clear()
        for (i in 0 until (mCols * mRows / 2)) {
            arrPict.add(pictureCollection + i)
            arrPict.add(pictureCollection + i)
        }
        arrPict.shuffle()
    }
    private fun closeAllCells() {
        arrStatus.clear()
        for (i in 0 until (mCols * mRows)) arrStatus.add(Status.CELL_CLOSE)
    }
}