package com.example.reminderapp.utils

import com.example.reminderapp.BuildConfig

object Constants {
    const val START_WORK: String = "START_WORK"
    const val STOP_ALARM: String = "STOP_ALARM"
    const val START_ALARM: String = "START_ALARM"
    const val foregroundNotificationId = "Foreground Notification Id"
    const val DailyServiceAction = BuildConfig.APPLICATION_ID + ".Action.DailyService"
    const val CHANNEL_ID = "CHANNEL_ID"
    const val CHANNEL_NAME = "CHANNEL_NAME"

    const val NOTIFICATION_ACTION = "NOTIFICATION_ACTION"
    const val ALARM_ACTION = "ALARM_ACTION"
    const val STOP_ACTION = "STOP_ACTION"

    const val INTENT_EXTRA_ID = "INTENT_EXTRA"
}