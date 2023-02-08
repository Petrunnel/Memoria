package com.petrunnel.memoria.records

import android.annotation.SuppressLint
import android.content.Context

class RecordTimeAdapter(context: Context): RecordAdapter(context) {

    override fun getItemCount(): Int = recordsFileIO.recTime.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = recordsFileIO.recTime[position]
        holder.item.text = "${position + 1}) $item"
    }
}