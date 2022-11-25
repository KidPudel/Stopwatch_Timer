package org.hyperskill.stopwatch.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.os.Handler
import android.os.Looper
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class StopwatchViewModel : ViewModel() {
    private var totalSeconds = 0
    private var minutes = 0
    private var seconds = 0

    // to convert timer value
    private val timeFormatter = SimpleDateFormat("mm.ss")

    private val timer = MutableLiveData<String>().apply { postValue("00:00") }
    val getTimer: LiveData<String>
        get() = timer


    // get an access to a main thread and put runnable in it
    private val handler = Handler(Looper.getMainLooper())

    // runnable to be executed in a thread loop
    private val updateTimer: Runnable = object : Runnable {
        override fun run() {
            totalSeconds += 1
            minutes = (totalSeconds % 3600) / 60 // limit to 59 minutes, if 3601 -> 1h, 0m, 1s
            seconds = totalSeconds % 60 // limit to 59 seconds
            // convert it from double to date format
            timer.value = String.format("%02d:%02d", minutes, seconds)

            // after it fires in message queue and send to handler, we post it again with the same delay
            handler.postDelayed(this, 1000)
        }
    }

    // handler.hasCallbacks(Runnable) but for older minSDK than 29
    private var callbackPresent = false

    fun startTimer() {
        // update timer every second 0:00..0:01, add runnable to a thread loop
        if (!callbackPresent) {
            handler.postDelayed(updateTimer, 1000)
            callbackPresent = true
        }
    }

    fun resetTimer() {
        totalSeconds = 0
        timer.value = "00:00"
        handler.removeCallbacks(updateTimer)
        callbackPresent = false
    }

    fun removeCallbacks() {
        handler.removeCallbacks(updateTimer)
    }
}