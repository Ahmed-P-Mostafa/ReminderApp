package com.example.reminderapp.ui.newLecture

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.reminderapp.R
import com.example.reminderapp.databinding.ActivityNewLectureBinding
import com.example.reminderapp.pojo.database.LecturesDatabase
import com.example.reminderapp.pojo.models.Lecture
import com.example.reminderapp.ui.base.BaseActivity
import com.example.reminderapp.ui.home.HomeActivity
import com.example.reminderapp.utils.Constants
import com.example.reminderapp.utils.DatePickerFragment
import com.example.reminderapp.utils.TimePickerFragment
import com.example.reminderapp.utils.services.AlarmReceiver
import java.util.*
import kotlin.math.log

class NewLectureActivity : BaseActivity<ActivityNewLectureBinding, NewLectureViewModel>(),Navigator {
    private val TAG = "NewLectureActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding.vm = viewModel
        viewModel.navigator = this

        val platformList = listOf("Zoom","Google Meet")
        observe()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, platformList)
        (binding.platformTil.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.attendanceRg.setOnCheckedChangeListener { group, checkedId ->

            when(checkedId){
                R.id.radioButtonOnline->{
                    binding.PlatformLabel.text = "Platform"
                    binding.platformTil.visibility = View.VISIBLE
                    binding.locationTil.visibility = View.GONE
                    viewModel.lectureType.value = "Online"
                    binding.typeRg.check(R.id.even_rb)
                }
                R.id.radioButtonOffline->{
                    binding.PlatformLabel.text = "Location"
                    binding.platformTil.visibility = View.GONE
                    binding.locationTil.visibility = View.VISIBLE
                    viewModel.lectureType.value = "Offline"
                    binding.typeRg.check(R.id.odd_rb)

                }
            }
        }
        binding.typeRg.setOnCheckedChangeListener { group, checkedId ->

            Log.d(TAG, "onCreate: $checkedId")
            when(checkedId){
                R.id.even_rb->{
                    viewModel.repeat = 14
                }
                R.id.odd_rb->{
                    viewModel.repeat = 7

                }
            }
        }
        binding.dateTil.setEndIconOnClickListener {
            showDatePicker()
        }
    }

    override fun getLayoutId(): Int {
        return  R.layout.activity_new_lecture
    }

    override fun initializeViewModel(): NewLectureViewModel {
        return ViewModelProvider(this).get(NewLectureViewModel::class.java)
    }

    private fun showDatePicker(){
        Log.e(TAG, "showDatePicker: ")
        val dialogFragment : DialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "Date")
    }

    fun processDatePickerResult(day: Int, month: Int, year: Int){
        showTimePicker()
        val month_string = (month + 1).toString()
        val day_string = day.toString()
        val year_string = year.toString()
        val dateMessage = month_string +
                "/" + day_string + "/" + year_string
        binding.dateEt.setText(dateMessage)
        viewModel.year.value = year
        viewModel.month.value = month
        viewModel.day.value = day
    }

    private fun showTimePicker() {
        val dialogFragment :DialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager,"Time")
    }

    fun processTimePickerResult(hours:Int, minutes:Int){
        viewModel.hours.value = hours
        viewModel.minutes.value = minutes
    }

    override fun backToHome() {
        startActivity(Intent(this,HomeActivity::class.java))
        finish()
    }

    override fun insertLectureToDatabase(lecture: Lecture) {
        Log.d(TAG, "insertLectureToDatabase: ")
        if(binding.attendanceRg.checkedRadioButtonId == R.id.radioButtonOnline){
            lecture.location = binding.platformEt.text.toString()
        }else{
            lecture.location = binding.locationEt.text.toString()
        }


            val id = LecturesDatabase.getInstance(this).lecturesDAO().insert(lecture)
            //Toast.makeText(this@NewLectureActivity, raw.toString(), Toast.LENGTH_SHORT).show()
        if (checkForLectureDate(lecture)){
            Log.d(TAG, "insertLectureToDatabase: yes ")
            Log.d(TAG, "insertLectureToDatabase: id = $id")
            Log.d(TAG, "insertLectureToDatabase: time = ${lecture.time}")


            scheduleAlarm(id.toInt(),lecture.time?:Calendar.getInstance().timeInMillis)
        }


        startActivity(Intent(this,HomeActivity::class.java))
        finish()
    }

    private fun observe() {
        viewModel.hours.observe(this, Observer {
            Log.d(TAG, "observe: hours ${it.toString()}")
        })
        viewModel.minutes.observe(this, Observer {
            Log.d(TAG, "observe:minutes  ${it.toString()}")
        })

        viewModel.day.observe(this, Observer {
            Log.d(TAG, "observe: day ${it.toString()}")
        })
        viewModel.month.observe(this, Observer {
            Log.d(TAG, "observe: month ${it.toString()}")
        })
        viewModel.year.observe(this, Observer {
            Log.d(TAG, "observe: year ${it.toString()}")
        })
        viewModel.name.observe(this, Observer {
            Log.d(TAG, "observe: name ${it.toString()}")
        })
        viewModel.code.observe(this, Observer {
            Log.d(TAG, "observe: code ${it.toString()}")
        })
        viewModel.lectureType.observe(this, Observer {
            Log.d(TAG, "observe: type ${it.toString()}")
        })
        viewModel.location.observe(this, Observer {
            Log.d(TAG, "observe: location ${it.toString()}")
        })

    }

    private fun scheduleAlarm(id:Int, trigger: Long) {
        Log.d(TAG, "scheduleAlarm: ")
        val hour = AlarmManager.INTERVAL_HOUR

        sendPriorNotification(id, trigger - hour)
        sendAlarm(id, trigger)

    }
    private fun checkForLectureDate(lecture: Lecture): Boolean {
        Log.d(TAG, "checkForLectureDate: ")
        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val lecDate = Calendar.getInstance()
        lecDate.timeInMillis = lecture.time!!
        val lecDay = lecDate.get(Calendar.DAY_OF_YEAR)

        return lecDay == today
    }
    private fun sendPriorNotification(id: Int, trigger: Long) {
        Log.d(TAG, "sendPriorNotification: ")
        
         val notificationIntent = Intent(this, AlarmReceiver::class.java)
      notificationIntent.action = Constants.NOTIFICATION_ACTION
      notificationIntent.putExtra(Constants.INTENT_EXTRA_ID, id)

      val notificationPendingIntent =
          PendingIntent.getBroadcast(this, id, notificationIntent, PendingIntent.FLAG_ONE_SHOT)

      val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
      alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, notificationPendingIntent)

    }

    private fun sendAlarm(id: Int, trigger: Long) {
        Log.d(TAG, "sendAlarm: ")
        val alarmIntent = Intent(this, AlarmReceiver::class.java)
        alarmIntent.action = Constants.ALARM_ACTION
        alarmIntent.putExtra(Constants.INTENT_EXTRA_ID, id)

        val alarmPendingIntent =
            PendingIntent.getBroadcast(this, id, alarmIntent, PendingIntent.FLAG_ONE_SHOT)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, alarmPendingIntent)
    }

}