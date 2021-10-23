package com.example.reminderapp.utils

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager

class RingtoneHelper {

    companion object{

        private var mInstance :Ringtone?=null

        fun getInstance(context: Context):Ringtone{
            if (mInstance==null){
                val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                mInstance = RingtoneManager.getRingtone(context,uri)
            }
            return mInstance!!
        }
    }
}