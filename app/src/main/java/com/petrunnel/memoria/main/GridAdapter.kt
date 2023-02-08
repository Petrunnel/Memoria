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
import kotlin.collections.ArrayList

class GridAdapter(
    private val mContext: Context,
    private val configuration: Configuration,
    private val itemClickListener: (View, Int, Int) -> Unit
) :
    RecyclerView.Adapter<GridAdapter.GridViewHolder>() {

    private val pictureCollection: String = configuration.pictureCollection
    private val mRes: Resources = mContext.resources
    private val isTriplets = configuration.type
    private val arrPict: ArrayList<String?> = ArrayList()
    private val arrStatus: ArrayList<Status> = ArrayList()

    enum class Status : Serializable {
        CELL_OPEN, CELL_CLOSE, CELL_DELETE
    }

    init {
        fieldInit()
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
            else -> holder.item.setImageResource(R.drawable.checked)
        }
    }

    override fun getItemCount(): Int {
        return configuration.getCols() * configuration.getRows()
    }

    fun checkOpenCells() {
        val arrStatusOpenIndex: ArrayList<Int> = ArrayList()
        for (i in 0 until arrStatus.size) {
            if (arrStatus[i] == Status.CELL_OPEN) arrStatusOpenIndex.add(i)
        }
        when (arrStatusOpenIndex.size) {
            2 -> {
                if (arrPict[arrStatusOpenIndex[0]] != arrPict[arrStatusOpenIndex[1]]) {
                    arrStatus[arrStatusOpenIndex[0]] = Status.CELL_CLOSE
                    arrStatus[arrStatusOpenIndex[1]] = Status.CELL_CLOSE
                }
                if (!isTriplets && arrPict[arrStatusOpenIndex[0]] == arrPict[arrStatusOpenIndex[1]]) {
                    arrStatus[arrStatusOpenIndex[0]] = Status.CELL_DELETE
                    arrStatus[arrStatusOpenIndex[1]] = Status.CELL_DELETE
                }
            }
            3 -> {
                if (arrPict[arrStatusOpenIndex[0]] != arrPict[arrStatusOpenIndex[1]] || arrPict[arrStatusOpenIndex[1]] != arrPict[arrStatusOpenIndex[2]]) {
                    arrStatus[arrStatusOpenIndex[0]] = Status.CELL_CLOSE
                    arrStatus[arrStatusOpenIndex[1]] = Status.CELL_CLOSE
                    arrStatus[arrStatusOpenIndex[2]] = Status.CELL_CLOSE

                } else {
                    arrStatus[arrStatusOpenIndex[0]] = Status.CELL_DELETE
                    arrStatus[arrStatusOpenIndex[1]] = Status.CELL_DELETE
                    arrStatus[arrStatusOpenIndex[2]] = Status.CELL_DELETE

                }
            }
        }
        arrStatusOpenIndex.forEach {
            notifyItemChanged(it)
        }
    }

    fun openCell(position: Int): Boolean {
        if (arrStatus[position] == Status.CELL_DELETE ||
            arrStatus[position] == Status.CELL_OPEN
        ) return false
        if (arrStatus[position] != Status.CELL_DELETE) {
            arrStatus[position] = Status.CELL_OPEN
        }
        notifyItemChanged(position)
        return true
    }
    fun checkGameOver(): Boolean {
        return !arrStatus.contains(Status.CELL_CLOSE)
    }
    fun fieldInit() {
        makePictArray()
        closeAllCells()
        notifyItemRangeChanged(0, itemCount)
    }

    fun getArrPictCells() = arrPict
    fun getArrStatusCells() = arrStatus
    private fun makePictArray() {
        arrPict.clear()
        if (isTriplets) {
            for (i in 0 until (itemCount / 3)) {
                arrPict.add(pictureCollection + i)
                arrPict.add(pictureCollection + i)
                arrPict.add(pictureCollection + i)
            }
        } else {
            for (i in 0 until (itemCount / 2)) {
                arrPict.add(pictureCollection + i)
                arrPict.add(pictureCollection + i)
            }
        }
        arrPict.shuffle()
    }

    private fun closeAllCells() {
        arrStatus.clear()
        for (i in 0 until (itemCount)) arrStatus.add(Status.CELL_CLOSE)
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