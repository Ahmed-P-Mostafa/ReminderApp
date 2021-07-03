package com.example.reminderapp.ui.base

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.example.reminderapp.utils.Constants.CHANNEL_ID
import com.example.reminderapp.utils.Constants.CHANNEL_NAME


abstract class BaseActivity<DB : ViewDataBinding, VM : BaseViewModel<*>> : AppCompatActivity() {
    private val TAG = "TAG BaseActivity"
    lateinit var viewModel: VM
    lateinit var binding: DB
    private var loader: ProgressDialog? = null
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        viewModel = initializeViewModel()
        observers()
        sharedPreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE)
        if (!sharedPreferences.getBoolean("AUTO_START",false)){
            checkForDeviceManufacture()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createChannel()
    }

    abstract fun getLayoutId(): Int
    abstract fun initializeViewModel(): VM


    fun showMessage(message: String) {
        AlertDialog.Builder(this).setMessage(message).setCancelable(false)
            .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            }).show()
    }

     fun showDialog(
        title: String,
        message: String,
        posBtnText: String,
        posBtnClickListener: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(posBtnText, posBtnClickListener)

            .show()
    }

    fun showLoader() {
        loader = ProgressDialog(this)
        loader?.apply { ->
            setMessage("Loading...")
            setCancelable(false)
            show()
        }

    }

    fun hideLoader() {
        if (loader != null && loader?.isShowing!!) {
            loader?.dismiss()
        }
    }


    private fun observers() {
        viewModel.message.observe(this, Observer {
            showMessage(it)
        })
        viewModel.loader.observe(this, Observer {
            when (it) {
                true -> showLoader()
                false -> hideLoader()
            }
        })

    }

    fun login() {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("UserLogin", true).apply()
    }

    fun logout() {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("UserLogin", false).apply()
    }

    fun isUserLogin(): Boolean {
        return sharedPreferences.getBoolean("UserLogin", false)
    }

    private fun setAutoStart(){
        val editor :SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("AUTO_START",true).apply()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        channel.description = "CHANNEL_DESCRIPTION"
        channel.lightColor = Color.GREEN
        channel.enableLights(true)
        channel.enableVibration(true)
        manager.createNotificationChannel(channel)
    }

    // todo continue this task
    private fun checkForDeviceManufacture() {
        try {
            val intent = Intent()
            val manufacturer = Build.MANUFACTURER
            if ("xiaomi".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.autostart.AutoStartManagementActivity"
                )
            } else if ("oppo".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName(
                    "com.coloros.safecenter",
                    "com.coloros.safecenter.permission.startup.StartupAppListActivity"
                )
            } else if ("vivo".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName(
                    "com.vivo.permissionmanager",
                    "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
                )
            } else if ("Letv".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName(
                    "com.letv.android.letvsafe",
                    "com.letv.android.letvsafe.AutobootManageActivity"
                )
            } else if ("Honor".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName(
                    "com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.process.ProtectActivity"
                )
            }
            val list =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (list.size > 0) {

                showDialog(
                    title = "Application need Permission",
                    message = "${manufacturer.toUpperCase()} Devices need to allow \"auto start\" permission ", posBtnText =
                    "Ok",posBtnClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                        setAutoStart()
                        startActivity(intent)
                    })
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }


}