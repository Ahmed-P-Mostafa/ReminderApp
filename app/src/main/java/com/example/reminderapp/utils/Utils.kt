package com.example.reminderapp.utils

import android.content.Context
import android.database.DatabaseUtils
import com.example.reminderapp.di.DatabaseModule
import com.example.reminderapp.pojo.database.LecturesDatabase
import java.util.*

object Utils {

    fun checkForExistAlarm(context: Context):Int{
        val todayCalendar = Calendar.getInstance()
        todayCalendar.timeInMillis = Calendar.getInstance().timeInMillis
        val today = todayCalendar.get(Calendar.DAY_OF_YEAR)
        val list = LecturesDatabase.getInstance(context = context).lecturesDAO().getAllLectures()
        for (i in 0..list.size){
            val lectureCalendar = Calendar.getInstance()
            lectureCalendar.timeInMillis = list[i].time!!
            val lectureDay = lectureCalendar.get(Calendar.DAY_OF_YEAR)
            if (today==lectureDay){
                return list[i].id!!
            }

        }
        return -1
    }
    fun checkForExistAlarm(context: Context,id:Int):Boolean{
        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val lecture = LecturesDatabase.getInstance(context = context).lecturesDAO().getLecture(id = id)

            val lectureCalendar = Calendar.getInstance()
            lectureCalendar.timeInMillis = lecture.time!!
            val lectureDay = lectureCalendar.get(Calendar.DAY_OF_YEAR)

           return today == lectureDay
    }
}