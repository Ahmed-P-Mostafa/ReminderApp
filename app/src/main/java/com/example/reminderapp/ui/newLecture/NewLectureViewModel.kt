package com.example.reminderapp.ui.newLecture

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.reminderapp.pojo.models.Lecture
import com.example.reminderapp.ui.base.BaseViewModel
import java.util.*

class NewLectureViewModel : BaseViewModel<Navigator>() {
    private val TAG = "TAG NewLectureViewModel"

    var name = MutableLiveData<String>()
    var code = MutableLiveData<String>()
    var location = MutableLiveData<String>()
    var lectureType = MutableLiveData<String>("Online")
    var hours = MutableLiveData<Int>()
    var minutes = MutableLiveData<Int>()
    var year = MutableLiveData<Int>()
    var month = MutableLiveData<Int>()
    var day = MutableLiveData<Int>()
    var repeat: Int = 14


    fun confirm() {
        Log.d(TAG, "observe confirm: repeat = $repeat")
        Log.d(TAG, "observe confirm: location = $location")
        if (isDataValid()) {

            val time = getDate()
            Log.d(TAG, "time from activity: $time")
            Log.d(TAG, "time from calendar: ${Calendar.getInstance().timeInMillis}")

            val lecture = Lecture(
                name = name.value,
                code = code.value,
                location = location.value,
                type = lectureType.value,
                repeatInterval = repeat,
                time = time

            )
            Log.d(TAG, "confirm: repeat days $repeat")
            navigator?.insertLectureToDatabase(lecture)

        } else {
            message.value = "Kindly make sure you fulfill all the information"
        }


    }

    private fun getDate(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year.value!!)
        calendar.set(Calendar.MONTH, month.value!!)
        calendar.set(Calendar.DAY_OF_MONTH, day.value!!)
        calendar.set(Calendar.HOUR_OF_DAY, hours.value!!)
        calendar.set(Calendar.MINUTE, minutes.value!!)
        return calendar.timeInMillis
    }

    fun isDataValid(): Boolean {
        return (name.value != null && code.value != null && lectureType.value != null
                && hours.value != null && minutes.value != null && year.value != null && month.value != null
                && day.value != null)
    }

    private fun checkForLectureDate(lecture: Lecture): Boolean {
        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val lecDate = Calendar.getInstance()
        lecDate.timeInMillis = lecture.time!!
        val lecDay = lecDate.get(Calendar.DAY_OF_YEAR)

        return lecDay == today
    }

}