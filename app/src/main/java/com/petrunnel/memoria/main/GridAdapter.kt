package com.petrunnel.memoria.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.petrunnel.memoria.R
import com.wajahatkarim3.easyflipview.EasyFlipView
import java.io.Serializable
import java.util.*

class GridAdapter(
    private val mContext: Context,
    private val configuration: Configuration,
    private val itemClickListener: (View, Int, Int) -> Unit
) :
    RecyclerView.Adapter<GridAdapter.GridViewHolder>() {

    companion object {
        private const val NEXT_CLICK_DELAY = 400L
    }

    private val pictureCollection: String = configuration.pictureCollection
    private val mRes: Resources = mContext.resources
    private val isTriplets = configuration.type
    private val arrPict: ArrayList<String?> = ArrayList()
    private val arrStatus: ArrayList<Status> = ArrayList()
    private var isClickAllowed: Boolean = true

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
        @SuppressLint("DiscouragedApi")
        val drawableId =
            mRes.getIdentifier(arrPict[position], "drawable", mContext.packageName)
        holder.itemFront.setImageResource(drawableId)

        when (arrStatus[position]) {
            Status.CELL_CLOSE -> {
                holder.itemBack.setImageResource(R.drawable.close)
                if (holder.cell.isFrontSide)
                    holder.cell.flipTheView()
            }
            Status.CELL_DELETE -> {
                holder.cell.visibility = View.INVISIBLE
            }
            Status.CELL_OPEN -> {
                if (holder.cell.isBackSide)
                    holder.cell.flipTheView()
            }
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
        arrStatusOpenIndex.forEach { index ->
            notifyItemChanged(index)
        }
    }

    fun openCell(position: Int): Boolean {

        isClickAllowed = false
        /*
        This delay need to prevent open next cell before flip animation will finished
         */
        Handler(Looper.getMainLooper()).postDelayed({
            isClickAllowed = true
        }, NEXT_CLICK_DELAY)

        if (arrStatus[position] == Status.CELL_DELETE ||
            arrStatus[position] == Status.CELL_OPEN
        ) return false
        if (arrStatus[position] != Status.CELL_DELETE)
            arrStatus[position] = Status.CELL_OPEN
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

    fun getIsClickAllowed() = isClickAllowed

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
        var cell: EasyFlipView = view.findViewById(R.id.cell)
        var itemBack: ImageView = view.findViewById(R.id.cellBack)
        var itemFront: ImageView = view.findViewById(R.id.cellFront)
    }
}