package com.petrunnel.memoria.records

import android.content.Context

class RecordTimeAdapter(context: Context): RecordAdapter(context) {

    override fun getItemCount(): Int = recordsFileIO.recTime.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = recordsFileIO.recTime[position]
        holder.item.text = item
    }
}