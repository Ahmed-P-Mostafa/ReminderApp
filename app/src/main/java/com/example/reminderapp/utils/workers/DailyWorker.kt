package com.example.reminderapp.utils.workers

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.reminderapp.R
import com.example.reminderapp.pojo.database.LecturesDatabase
import com.example.reminderapp.pojo.models.Lecture
import com.example.reminderapp.ui.home.HomeActivity
import com.example.reminderapp.utils.Constants.CHANNEL_ID
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

class DailyWorker(private val context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        dailyProcess()

        return Result.success()
    }

    private fun dailyProcess() {

        val list = LecturesDatabase.getInstance(context).lecturesDAO().getAllLectures()

        for (i in 0..list.size) {
            val lecture = list[i]
            val lectureDate = lecture.time!!

            if (isDateIsToday(lectureDate)) {

                showNotification(lecture)
                makeAlarmOnce(lecture)

            }
        }
    }

    private fun isDateIsToday(date: Long): Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = Calendar.getInstance().timeInMillis
        val calendarDay = calendar.get(Calendar.DAY_OF_YEAR)
        val lectureDate = Calendar.getInstance()
        lectureDate.timeInMillis = date
        val lectureDay = lectureDate.get(Calendar.DAY_OF_YEAR)

        return (lectureDay == calendarDay)
    }

    private fun makeAlarmOnce(lecture: Lecture) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, HomeActivity::class.java)
        intent.putExtra("ID",lecture.id)
        intent.putExtra("TITLE",lecture.name)
        intent.putExtra("TIME",lecture.time)
        val pendingIntent = PendingIntent.getActivity(
            context,
            lecture.id ?: Calendar.getInstance().timeInMillis.toInt(), intent, 0
        )

        alarmManager.set(AlarmManager.RTC_WAKEUP, lecture.time!!, pendingIntent)

    }

    private fun showNotification(lecture: Lecture) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, HomeActivity::class.java)
        val code = Random.nextInt(IntRange(1, 1000))
        val pendingIntent = PendingIntent.getActivity(context, code, intent, 0)
        val hoursFormat = SimpleDateFormat("HH:mm")

        val date = Calendar.getInstance()
        date.timeInMillis = lecture.time ?: 42115355L
        val hour = hoursFormat.format(date.time)

        val message = "Next lecture on : $hour"

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_calendar)
            .setContentTitle(lecture.name)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        val id = Random.nextInt(IntRange(1, 1000))
        manager.notify(id, notification)
    }


}