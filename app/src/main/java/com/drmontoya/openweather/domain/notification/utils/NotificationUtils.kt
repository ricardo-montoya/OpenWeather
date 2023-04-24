package com.drmontoya.openweather.domain.notification.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.drmontoya.openweather.MainActivity
import com.drmontoya.openweather.R


object NotificationConstants {
    const val TITLE = "notification_title"
    const val BODY = "notification_body"
    const val IMAGE = "notification_image"
    const val REQUEST_CODE = 0
}

val NOTIFICATION_ID: Int = 0

fun NotificationManager.sendNotification(
    image: Int,
    title: String,
    body: String,
    context: Context
) {
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        context,
        NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val weatherImage = BitmapFactory.decodeResource(
        context.resources,
        image
    )
    val bigPicture = NotificationCompat.BigPictureStyle()
        .bigPicture(weatherImage)
        .bigLargeIcon(null)

    val builder = NotificationCompat.Builder(
        context,
        context.getString(R.string.todays_forecast_channel_id)
    )
        .setSmallIcon(image)
        .setContentTitle(title)
        .setContentText(body)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setStyle(bigPicture)
        .setLargeIcon(weatherImage)

    notify(NOTIFICATION_ID, builder.build())
}