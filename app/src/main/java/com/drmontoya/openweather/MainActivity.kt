package com.drmontoya.openweather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.drmontoya.openweather.databinding.ActivityMainBinding
import com.drmontoya.openweather.domain.work_manager.NotifyWeatherWorker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setupWorkers()
        setContentView(binding.root)
    }

    private fun setupWorkers() {
        NotifyWeatherWorker.scheduleHourlyTask(this)
    }
}