package com.petrunnel.memoria.records

import android.annotation.SuppressLint
import android.content.Context

class RecordPointAdapter(context: Context): RecordAdapter(context) {

    override fun getItemCount(): Int = recordsFileIO.recPoint.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = recordsFileIO.recPoint[position]
        holder.item.text = "${position + 1}) $item"
    }
}