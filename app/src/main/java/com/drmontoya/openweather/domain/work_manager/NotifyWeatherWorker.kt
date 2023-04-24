package com.drmontoya.openweather.domain.work_manager

import android.app.NotificationManager
import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.drmontoya.openweather.data.local.entity.asDTO
import com.drmontoya.openweather.domain.notification.utils.sendNotification
import com.drmontoya.openweather.domain.repository.ForecastRepository
import com.drmontoya.openweather.domain.repository.LocationRepository
import com.drmontoya.openweather.presentation.fragments.home.HomeFragment
import com.drmontoya.openweather.presentation.fragments.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotifyWeatherWorker
constructor(
    val context: Context,
    val workerParameters: WorkerParameters

) : CoroutineWorker(context, workerParameters) {

    @Inject
    lateinit var forecastRepository: ForecastRepository

    @Inject
    lateinit var locationRepository: LocationRepository

    override suspend fun doWork(): Result {
        val locationId = getSavedLocation(context = context)
        return if (locationId != 0) {

            withContext(Dispatchers.IO) {
                val location = locationRepository.getLocationById(locationId)
                val forecast = forecastRepository.getForecastByLatitudeAndLongitudeLocal(
                    latitude = location.latitude!!,
                    longitude = location.longitude!!
                )?.asDTO()
                if (forecast != null && location != null) {
                    val title = HomeViewModel.buildNotificationTitle(forecast, location)
                    val body = HomeViewModel.buildNotificationBody(forecast)
                    val image = HomeViewModel.getTodaysWeatherImage(forecast)
                    val notificationManager =
                        context.getSystemService(NotificationManager::class.java)
                    notificationManager.sendNotification(
                        image = image,
                        body = body,
                        title = title,
                        context = context
                    )
                    Result.success()
                }
                Result.failure()
            }
        } else {
            Result.failure()
        }
    }

    fun getSavedLocation(context: Context): Int {
        val prefs =
            context.getSharedPreferences(HomeFragment.LOCAL_PREFERENCES, Context.MODE_PRIVATE)
        return prefs.getInt(HomeFragment.SELECTED_LOCATION, 0)
    }

    companion object {
        private const val TAG = "weather_automatic_notification"
        fun scheduleHourlyTask(contex: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val weatherNotificationWork =
                PeriodicWorkRequestBuilder<NotifyWeatherWorker>(1, TimeUnit.HOURS)
                    .setConstraints(constraints)
                    .build()
            WorkManager.getInstance(contex).enqueueUniquePeriodicWork(
                TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                weatherNotificationWork
            )
        }
    }
}