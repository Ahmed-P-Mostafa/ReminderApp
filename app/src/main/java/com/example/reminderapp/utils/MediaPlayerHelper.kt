package com.example.reminderapp.utils

import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager

class MediaPlayerHelper {

    companion object{

        private var mInstance :MediaPlayer?=null

        fun getInstance(context: Context):MediaPlayer{
            if (mInstance==null){
                val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                mInstance = MediaPlayer.create(context,uri)
            }
            return mInstance!!
        }
    }
}