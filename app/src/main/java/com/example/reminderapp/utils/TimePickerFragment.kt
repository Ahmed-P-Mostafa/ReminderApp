package com.example.reminderapp.utils

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.text.format.Time
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.reminderapp.ui.newLecture.NewLectureActivity
import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeoutException

class TimePickerFragment :DialogFragment(),TimePickerDialog.OnTimeSetListener {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
       val c = Calendar.getInstance()

        val hours:Int = c.get(Calendar.HOUR_OF_DAY)
        val minutes:Int = c.get(Calendar.MINUTE)


        return TimePickerDialog(requireContext(),this,hours,minutes,DateFormat.is24HourFormat(requireContext()))
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val activity : NewLectureActivity? = getActivity() as NewLectureActivity?
        // val activity: MainActivity? = getActivity() as MainActivity?
        activity?.processTimePickerResult(hourOfDay,minute)
    }
}