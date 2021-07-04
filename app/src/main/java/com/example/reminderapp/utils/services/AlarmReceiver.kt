package com.example.reminderapp.utils.services

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.Ringtone
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.reminderapp.R
import com.example.reminderapp.pojo.database.LecturesDatabase
import com.example.reminderapp.ui.AlarmActivity
import com.example.reminderapp.ui.home.HomeActivity
import com.example.reminderapp.utils.Constants
import com.example.reminderapp.utils.Constants.CHANNEL_ID
import com.example.reminderapp.utils.RingtoneHelper
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    private var mNotificationManager: NotificationManager? = null
    private val TAG = "AlarmReceiver"
    private var mediaPlayer: Ringtone? = null
    private var title = ""
    private var body = ""
    private var id = 0
    private var time = 0L
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onReceive(context: Context, intent: Intent) {
        mediaPlayer = RingtoneHelper.getInstance(context)
       // mediaPlayer!!.isLooping = true
        id = intent.getIntExtra(Constants.INTENT_EXTRA_ID, 0)


        mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        when (intent.action) {
            // make alarm start and send notification
            Constants.ALARM_ACTION -> {
                val lecture = LecturesDatabase.getInstance(context).lecturesDAO().getLecture(id)

                title = lecture.name!!
                body = "${lecture.name} time is up at ${lecture.location}"


                startAlarm(context)
                deliverAlarmNotification(
                    context,
                    intent.getIntExtra(Constants.INTENT_EXTRA_ID, 0),
                    title,
                    body
                )
                updateLectureNextDate(id, context)
            } // stop button in notification
            Constants.STOP_ACTION -> {
                stopAlarm(id)
            }
            // send prior notification for notice.
            Constants.NOTIFICATION_ACTION -> {

                val lecture = LecturesDatabase.getInstance(context).lecturesDAO().getLecture(id)
                val formatter = SimpleDateFormat("h:mm a")
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = lecture.time!!
                body = "Next lecture at: ${formatter.format(calendar.time)}"
                title = lecture.name!!
                deliverPriorNotification(context, title, body, id)

            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun startAlarm(context: Context) {
        if (mediaPlayer != null) {
            Log.d(TAG, "startAlarm: alarm started and != null")
            //mediaPlayer?.prepare()
            mediaPlayer?.play()

        }else{
            Log.d(TAG, "startAlarm: alarm not started and == null")
            mediaPlayer = RingtoneHelper.getInstance(context = context)
            mediaPlayer!!.isLooping = true
        }
    }

    private fun stopAlarm(id: Int) {
        mediaPlayer?.stop()
        //mediaPlayer?.release()
        mNotificationManager?.cancel(id)
        Log.d(TAG, "stopAlarm: stop alarm")
    }


    private fun deliverAlarmNotification(context: Context, id: Int, title: String, body: String) {
        Log.d(TAG, "deliverNotification: ")
        val contentIntent = Intent(context, AlarmActivity::class.java)
        contentIntent.action = Constants.STOP_ACTION
        contentIntent.putExtra(Constants.INTENT_EXTRA_ID, id)


        val stopIntent = Intent(context.applicationContext, AlarmReceiver::class.java)
        stopIntent.action = Constants.STOP_ACTION
        stopIntent.putExtra(Constants.INTENT_EXTRA_ID, id)

        val pendingIntent: PendingIntent =
            PendingIntent.getBroadcast(
                context, id, stopIntent, PendingIntent.FLAG_ONE_SHOT
            )

        val stopAction = NotificationCompat.Action(0, "Stop", pendingIntent)

        val contentPendingIntent = PendingIntent.getActivity(
            context,
            id,
            contentIntent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val notificationBuilder =
            NotificationCompat.Builder(context.applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_calendar)
                .addAction(stopAction)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        mNotificationManager?.notify(id, notificationBuilder.build());
    }

    private fun updateLectureNextDate(id: Int, context: Context) {
        val lecture = LecturesDatabase.getInstance(context).lecturesDAO().getLecture(id)

        lecture.time = lecture.time?.plus(AlarmManager.INTERVAL_DAY * lecture.repeatInterval)
        LecturesDatabase.getInstance(context).lecturesDAO().update(lecture = lecture)
    }

    private fun deliverPriorNotification(context: Context, title: String, body: String, id: Int) {
        Log.d(TAG, "deliverPriorNotification: ")
        val contentIntent = Intent(context, HomeActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            id,
            contentIntent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val notificationBuilder =
            NotificationCompat.Builder(context.applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_calendar)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
        val random = Random().nextInt()
        mNotificationManager?.notify(random, notificationBuilder.build());
    }

}