package com.example.reminderapp.ui

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.reminderapp.R
import com.example.reminderapp.utils.Constants
import com.example.reminderapp.utils.services.AlarmReceiver
import kotlin.system.exitProcess

class AlarmActivity : AppCompatActivity() {
    var i :Intent?=null
    var id :Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
        i = intent
        id =   i?.getIntExtra(Constants.INTENT_EXTRA_ID,0)!!



    }

    // send broadcast to alarm receiver to stop alarm if user click on the notification itself not the stop button.
    fun stopAlarm(view: View) {
        val stopIntent = Intent(this,AlarmReceiver::class.java)
        stopIntent.action = Constants.STOP_ACTION
        stopIntent.putExtra(Constants.INTENT_EXTRA_ID,id)

        sendBroadcast(stopIntent)
        finish()
        finishAffinity()
    }
}