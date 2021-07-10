package com.example.reminderapp.utils.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.*

class BootReceiver :BroadcastReceiver() {
    private val TAG = "TAG BootReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive: ")


       when(intent.action){
          Intent.ACTION_BOOT_COMPLETED -> {
              startDailyAlarmManager(context)
              checkForEdgeAlarms(context)
          }

       }

    }

    private fun startDailyAlarmManager(context: Context){
        Log.d(TAG, "startDailyAlarmManager: ")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = Calendar.getInstance().timeInMillis
        calendar.set(Calendar.HOUR,23)

        val dailyIntent = Intent(context,DailyService::class.java)
        val dailyPendingIntent = PendingIntent.getService(context,5005,dailyIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,AlarmManager.INTERVAL_DAY,dailyPendingIntent)
    }

    private fun checkForEdgeAlarms(context: Context){
        Log.d(TAG, "checkForEdgeAlarms: ${Calendar.getInstance().timeInMillis}")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = Calendar.getInstance().timeInMillis


        val dailyIntent = Intent(context,DailyService::class.java)
        val dailyPendingIntent = PendingIntent.getService(context,5001,dailyIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.set(AlarmManager.RTC_WAKEUP,time,dailyPendingIntent)
                context.startService(dailyIntent)

        
        Log.d(TAG, "checkForEdgeAlarms: ${Calendar.getInstance().timeInMillis}")
    }
}