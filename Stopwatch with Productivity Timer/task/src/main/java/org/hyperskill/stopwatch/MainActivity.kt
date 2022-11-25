package org.hyperskill.stopwatch

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.hyperskill.stopwatch.databinding.ActivityMainBinding
import org.hyperskill.stopwatch.viewmodels.StopwatchViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // lazy instantiation
    private val stopwatchViewModel: StopwatchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // make buttons do stuff that they deserve
        binding.startButton.setOnClickListener {
            stopwatchViewModel.startTimer()
        }

        binding.resetButton.setOnClickListener {
            stopwatchViewModel.resetTimer()
        }

        stopwatchViewModel.getTimer.observe(this) {
            binding.textView.text = it
        }
    }

    override fun onStop() {
        super.onStop()
        stopwatchViewModel.removeCallbacks()
    }
}