package com.example.reminderapp.ui.newLecture

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.reminderapp.pojo.models.Lecture
import com.example.reminderapp.ui.base.BaseViewModel
import java.util.*

class NewLectureViewModel : BaseViewModel<Navigator>() {
    private val TAG = "TAG NewLectureViewModel"

    var name = MutableLiveData<String>("web")
    var code = MutableLiveData<String>("code")
    var location = MutableLiveData<String>()
    var lectureType = MutableLiveData<String>()
    var hours = MutableLiveData<Int>(9)
    var minutes = MutableLiveData<Int>()
    var year = MutableLiveData<Int>()
    var month = MutableLiveData<Int>()
    var day = MutableLiveData<Int>(9)
    var repeat: Int = 0


    fun confirm() {
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