/*
 * Copyright (c) 2022 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.yourcompany.android.studdy.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.yourcompany.android.R
import com.yourcompany.android.studdy.MainActivity
import com.yourcompany.android.studdy.StuddyApplication

fun showNotification(
    context: Context,
    channelId: String,
    channelName: String,
    notificationId: Int,
    contentTitle: String
) {
  val startAppIntent = Intent(context, MainActivity::class.java)
  val startAppPendingIntent = PendingIntent.getActivity(
      context,
      0,
      startAppIntent,
      PendingIntent.FLAG_IMMUTABLE
  )

  val deleteIntent = Intent(context, AlarmNotificationDismissedBroadcastReceiver::class.java)
  val deletePendingIntent = PendingIntent.getBroadcast(
      context,
      0,
      deleteIntent,
      PendingIntent.FLAG_IMMUTABLE
  )

  val notificationBuilder = NotificationCompat.Builder(context, channelId)
      .setSmallIcon(R.drawable.splash_icon)
      .setContentTitle(contentTitle)
      .setPriority(NotificationCompat.PRIORITY_HIGH)
      .setCategory(NotificationCompat.CATEGORY_ALARM)
      .setFullScreenIntent(startAppPendingIntent, true)
      .setDeleteIntent(deletePendingIntent)
  val notification = notificationBuilder.build()
  val notificationManager = context.getSystemService(NotificationManager::class.java)

  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
      && notificationManager.getNotificationChannel(channelId) == null
  ) {
    notificationManager.createNotificationChannel(
        NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
    )
  }

  notificationManager.notify(notificationId, notification)
}

class AlarmNotificationDismissedBroadcastReceiver : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {
    val alarmRingtoneState = (context.applicationContext as StuddyApplication).alarmRingtoneState
    alarmRingtoneState.value?.stop()
    alarmRingtoneState.value = null
  }
}