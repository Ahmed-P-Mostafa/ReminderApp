package com.example.reminderapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.reminderapp.R
import com.example.reminderapp.utils.Constants
import com.example.reminderapp.utils.services.AlarmReceiver

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