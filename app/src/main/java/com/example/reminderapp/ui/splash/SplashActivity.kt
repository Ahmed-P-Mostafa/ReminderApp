package com.example.reminderapp.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProvider
import com.example.reminderapp.R
import com.example.reminderapp.databinding.ActivitySplashBinding
import com.example.reminderapp.ui.base.BaseActivity
import com.example.reminderapp.ui.base.BaseViewModel
import com.example.reminderapp.ui.home.HomeActivity
import com.example.reminderapp.ui.login.LoginActivity

class SplashActivity : BaseActivity<ActivitySplashBinding,BaseViewModel<*>>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed(Runnable {
            if (isUserLogin()){
                startActivity(Intent(this,HomeActivity::class.java))
            }else{
                startActivity(Intent(this,LoginActivity::class.java))
            }
            finish()
        },1000)

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initializeViewModel(): BaseViewModel<*> {
        return ViewModelProvider(this).get(BaseViewModel::class.java)
    }
}