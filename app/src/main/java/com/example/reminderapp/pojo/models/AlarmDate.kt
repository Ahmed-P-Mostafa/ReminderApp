package com.example.reminderapp.pojo.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

abstract class AlarmDate() {
    var date: Date = Date()
        get() {
            return field
        }
        @RequiresApi(Build.VERSION_CODES.O)
        set(value){
            field = value
            if (isNotificationDate(value)==true){

                notificationDate = getNewNotificationDate(value)!!
                alertDate = getNewAlertDate(value)
            }
        }

    private fun getNewAlertDate(date: Date): Date {

        if (date.minutes>20){
            return Date(date.year,date.month,date.day,date.hours,date.minutes-20)

        }else{
            if (date.hours>1){
                val minutes = (date.minutes - 20 ) - 60
                return Date(date.year,date.month,date.day,date.hours-1,minutes)
            }else{
                return Date(date.year,date.month,date.day-1,23,date.minutes)
            }

        }

    }

    var notificationDate:Date = Date()
        get() {
          return field
        }
        set(value) {
            field= value
        }

    var alertDate:Date
        get() {
            return alertDate
        }
        set(value) {
            this.alertDate = value
        }


    @RequiresApi(Build.VERSION_CODES.O)
    fun isNotificationDate(date:Date): Boolean? {
        val calendar = Calendar.getInstance()
        val  testCalendar = Calendar.getInstance()

        calendar.time= Date()
        testCalendar.time= date

        testCalendar.add(Calendar.HOUR_OF_DAY ,12)
        if (testCalendar.get(Calendar.DAY_OF_YEAR)>calendar.get(Calendar.DAY_OF_YEAR)){
            return true
        }else{
            if (testCalendar.get(Calendar.HOUR_OF_DAY)-calendar.get(Calendar.HOUR_OF_DAY)>12){
                return true
            }

            return false
        }


    }
    fun getNewNotificationDate(date: Date):Date?{
        return if (date.day>Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
            Date(date.year,date.month,date.day-1,date.hours,date.minutes)

        }else null

    }

}