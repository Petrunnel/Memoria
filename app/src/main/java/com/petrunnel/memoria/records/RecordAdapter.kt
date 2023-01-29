package com.petrunnel.memoria.records

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.petrunnel.memoria.R

abstract class RecordAdapter(context: Context): RecyclerView.Adapter<RecordAdapter.ViewHolder>() {
    private var mContext: Context = context
    protected var recordsFileIO: RecordsFileIO = RecordsFileIO(mContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item, parent, false))
    }

    abstract override fun getItemCount(): Int

    abstract override fun onBindViewHolder(holder: ViewHolder, position: Int)

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var item: TextView = view.findViewById(R.id.item)
    }
}