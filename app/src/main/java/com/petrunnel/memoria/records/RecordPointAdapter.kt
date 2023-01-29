package com.petrunnel.memoria.records

import android.content.Context

class RecordPointAdapter(context: Context): RecordAdapter(context) {

    override fun getItemCount(): Int = recordsFileIO.recPoint.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = recordsFileIO.recPoint[position]
        holder.item.text = item.toString()
    }
}