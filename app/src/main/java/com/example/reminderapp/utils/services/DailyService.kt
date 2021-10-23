package com.example.reminderapp.utils.services

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.reminderapp.R
import com.example.reminderapp.pojo.database.LecturesDatabase
import com.example.reminderapp.utils.Constants
import java.util.*

class DailyService : Service() {
    private val TAG = "TAG DailyService"

    var notification: Notification? = null
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")

        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)

        val list = LecturesDatabase.getInstance(context = this).lecturesDAO().getAllLectures()

        for (i in list.indices) {
            val lectureCalendar = Calendar.getInstance()
            lectureCalendar.timeInMillis = list[i].time!!
            val lectureDay = lectureCalendar.get(Calendar.DAY_OF_YEAR)
            if (today == lectureDay) {
                val hour = AlarmManager.INTERVAL_HOUR

                sendPriorNotification(list[i].id!!, list[i].time!! - hour)
                sendAlarm(list[i].id!!, list[i].time!!)

            }

        }



        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
        val notification = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_calendar).setContentTitle("alarm")
            .setContentText("ALARM BODY").setPriority(NotificationCompat.PRIORITY_HIGH).build()

        //startForeground(0, notification)
        //startForeground(0,Notification.Builder(this,Constants.CHANNEL_ID).build(),0)

    }


  /*  override fun onTaskRemoved(rootIntent: Intent?) {
        stopSelf()
        stopForeground(true)
    }*/


    private fun sendPriorNotification(id: Int, trigger: Long) {
        val notificationIntent = Intent(this, AlarmReceiver::class.java)

        notificationIntent.action = Constants.NOTIFICATION_ACTION

        notificationIntent.putExtra(Constants.INTENT_EXTRA_ID, id)

        val notificationPendingIntent =
            PendingIntent.getBroadcast(this, id, notificationIntent, PendingIntent.FLAG_ONE_SHOT)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, notificationPendingIntent)
    }

    private fun sendAlarm(id: Int, trigger: Long) {
        val alarmIntent = Intent(this, AlarmReceiver::class.java)
        alarmIntent.action = Constants.ALARM_ACTION
        alarmIntent.putExtra(Constants.INTENT_EXTRA_ID, id)

        val alarmPendingIntent =
            PendingIntent.getBroadcast(this, id, alarmIntent, PendingIntent.FLAG_ONE_SHOT)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, alarmPendingIntent)
    }



}