package com.petrunnel.memoria.records

import android.content.Context
import android.widget.Toast
import com.petrunnel.memoria.R
import java.io.File
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.ArrayList

class RecordsFileIO(context: Context) {
    var recTime: ArrayList<String>
    var recPoint: ArrayList<Int>
    private var mContext: Context

    init {
        recTime = ArrayList()
        recPoint = ArrayList()
        mContext = context
        @Suppress("UNCHECKED_CAST")
        try {
            val fis = mContext.openFileInput(FILE_RECORDS)
            val `is` = ObjectInputStream(fis)
            recPoint = `is`.readObject() as ArrayList<Int>
            recTime = `is`.readObject() as ArrayList<String>
            `is`.close()
        } catch (e: FileNotFoundException) {
            File(FILE_RECORDS)
        } catch (e: Exception) {
            Toast.makeText(mContext, R.string.io_read_error, Toast.LENGTH_LONG)
                .show()
        }
    }

    fun writeRecords() {
        try {
            val fos = mContext.openFileOutput(FILE_RECORDS, Context.MODE_PRIVATE)
            val os = ObjectOutputStream(fos)
            os.writeObject(recPoint)
            os.writeObject(recTime)
            os.close()
        } catch (e: Exception) {
            Toast.makeText(mContext, R.string.io_write_error, Toast.LENGTH_LONG)
                .show()
        }
    }

    fun addTime(str: String) {
        if (!recTime.contains(str)) recTime.add(str)
        recTime.sort()
        for (i in 10 until recTime.size) recTime.removeAt(i)
    }

    fun addPoint(num: Int) {
        if (!recPoint.contains(num)) recPoint.add(num)
        recPoint.sort()
        for (i in 10 until recPoint.size) recPoint.removeAt(i)
    }

    companion object {
        private const val FILE_RECORDS = "memoria-records"
    }
}