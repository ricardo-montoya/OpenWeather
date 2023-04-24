package com.drmontoya.openweather.domain.model.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.drmontoya.openweather.R
import com.drmontoya.openweather.domain.notification.utils.NotificationConstants
import com.drmontoya.openweather.domain.notification.utils.sendNotification


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val title = intent.getStringExtra(NotificationConstants.TITLE)
        val body = intent.getStringExtra(NotificationConstants.BODY)
        val image = intent.getIntExtra(NotificationConstants.IMAGE, R.drawable.sunny)
        notificationManager.sendNotification(
            image = image,
            title = title!!,
            body = body!!,
            context = context
        )
    }

}