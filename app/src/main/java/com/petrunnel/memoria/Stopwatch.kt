package com.petrunnel.memoria

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.text.format.DateUtils
import android.util.Log
import java.util.*

open class Stopwatch {

    interface OnStopwatchTickListener {
        fun onStopwatchTick(stopwatch: Stopwatch?)
    }

    companion object {
        private const val TAG = "Stopwatch"
    }

    private var mBase: Long = 0
    private var mNow: Long = 0
    private var mTimeWhenPaused: Long = 0
    private var text = ""
    private var mStarted = false
    private var mRunning = false
    private var mLogged = false
    private var mFormat: String? = null
    private var mFormatter: Formatter? = null
    private var mFormatterLocale: Locale? = null
    private val mFormatterArgs = arrayOfNulls<Any>(1)
    private var mFormatBuilder: StringBuilder? = null
    private val mRecycle = StringBuilder(8)

    var onStopwatchTickListener: OnStopwatchTickListener? = null
    private var base: Long
        get() = mBase
        set(base) {
            mBase = base
            dispatchStopwatchTick()
            updateText(SystemClock.elapsedRealtime())
        }

    init {
        mBase = SystemClock.elapsedRealtime()
        updateText(mBase)
    }

    fun restart() {
        reset()
        start()
    }

    fun reset() {
        stop()
        base = SystemClock.elapsedRealtime()
        mTimeWhenPaused = 0
    }

    fun resume() {
        base = SystemClock.elapsedRealtime() - mTimeWhenPaused
        start()
    }

    fun pause() {
        stop()
        mTimeWhenPaused = SystemClock.elapsedRealtime() - base
    }

    fun getText() = text

    fun dispatchStopwatchTick() {
        if (onStopwatchTickListener != null) {
            onStopwatchTickListener!!.onStopwatchTick(this)
        }
    }

    private fun start() {
        mStarted = true
        updateRunning()
    }
    private fun stop() {
        mStarted = false
        updateRunning()
    }

    @Synchronized
    private fun updateText(now: Long) {
        mNow = now
        var seconds = now - mBase
        seconds /= 1000
        if (seconds < 0) {
            seconds = -seconds
        }
        var text = DateUtils.formatElapsedTime(mRecycle, seconds)
        if (mFormat != null) {
            val loc = Locale.getDefault()
            if (mFormatter == null || loc != mFormatterLocale) {
                mFormatterLocale = loc
                mFormatter = Formatter(mFormatBuilder, loc)
            }
            mFormatBuilder!!.setLength(0)
            mFormatterArgs[0] = text
            try {
                mFormatter!!.format(mFormat, *mFormatterArgs)
                text = mFormatBuilder.toString()
            } catch (ex: IllegalFormatException) {
                if (!mLogged) {
                    Log.w(
                        TAG,
                        "Illegal format string: $mFormat"
                    )
                    mLogged = true
                }
            }
        }
        setText(text)
    }

    private fun setText(text: String) {
        this@Stopwatch.text = text
    }

    private fun updateRunning() {
        val running = mStarted
        if (running != mRunning) {
            if (running) {
                updateText(SystemClock.elapsedRealtime())
                dispatchStopwatchTick()
                Handler(Looper.getMainLooper()).postDelayed(mTickRunnable, 1000)
            } else {
               Handler(Looper.getMainLooper()).removeCallbacks(mTickRunnable)
            }
            mRunning = running
        }
    }

    private val mTickRunnable: Runnable = object : Runnable {
        override fun run() {
            if (mRunning) {
                updateText(SystemClock.elapsedRealtime())
                dispatchStopwatchTick()
                Handler(Looper.getMainLooper()).postDelayed(this, 1000)
            }
        }
    }
}