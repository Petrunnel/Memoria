package com.petrunnel.memoria.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.petrunnel.memoria.R
import java.io.Serializable
import java.util.*

class GridAdapter(
    private val mContext: Context,
    private val mCols: Int,
    private val mRows: Int,
    pictCollection: String,
    private val arrPict: ArrayList<String?> = ArrayList(),
    private val arrStatus: ArrayList<Status> = ArrayList(),
    private val itemClickListener: (View, Int, Int) -> Unit
) :
    RecyclerView.Adapter<GridAdapter.GridViewHolder>() {

    private val pictureCollection: String = pictCollection
    private val mRes: Resources = mContext.resources

    enum class Status : Serializable {
        CELL_OPEN, CELL_CLOSE, CELL_DELETE
    }

    init {
        if (arrPict.isEmpty()) {
            makePictArray()
            closeAllCells()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val gridViewHolder = GridViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.cell, parent, false)
        )
        gridViewHolder.onClick(itemClickListener)
        return gridViewHolder
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        when (arrStatus[position]) {
            Status.CELL_OPEN -> {
                @SuppressLint("DiscouragedApi")
                val drawableId =
                    mRes.getIdentifier(arrPict[position], "drawable", mContext.packageName)
                holder.item.setImageResource(drawableId)
            }
            Status.CELL_CLOSE -> holder.item.setImageResource(R.drawable.close)
            else -> holder.item.setImageResource(R.drawable.none)
        }
    }

    override fun getItemCount(): Int {
        return mCols * mRows
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
        notifyItemRangeChanged(0, mCols * mRows)
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

    private fun <T : RecyclerView.ViewHolder> T.onClick(event: (view: View, position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(it, adapterPosition, itemViewType)
        }
        return this
    }

    class GridViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var item: ImageView = view.findViewById(R.id.cell)
    }
}