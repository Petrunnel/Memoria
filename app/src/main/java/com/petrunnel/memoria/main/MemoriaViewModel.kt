package com.petrunnel.memoria.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petrunnel.memoria.Stopwatch

class MemoriaViewModel : ViewModel() {

    private var _stepCount = MutableLiveData(0)
    val stepCount: LiveData<Int>
        get() = _stepCount

    private var _timeText = MutableLiveData("")
    val timeText: LiveData<String>
        get() = _timeText

    val stopwatch = Stopwatch()
    init {
        _stepCount.value = 0
        stopwatch.onStopwatchTickListener = object : Stopwatch.OnStopwatchTickListener {
            override fun onStopwatchTick(stopwatch: Stopwatch?) {
                _timeText.postValue(stopwatch?.getText())
            }
        }
    }
    fun refresh() {
        _stepCount.value = 0
        stopwatch.restart()
    }
    fun incStepCount() {
        _stepCount.value = _stepCount.value?.plus(1)
    }
}