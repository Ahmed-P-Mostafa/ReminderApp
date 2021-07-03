package com.example.reminderapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.reminderapp.R
import com.example.reminderapp.databinding.ActivityLoginBinding
import com.example.reminderapp.databinding.ActivityRegisterBinding
import com.example.reminderapp.ui.base.BaseActivity
import com.example.reminderapp.ui.home.HomeActivity
import com.example.reminderapp.ui.register.RegisterActivity

class LoginActivity : BaseActivity<ActivityLoginBinding,LoginViewModel>(),Navigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.model = viewModel
        viewModel.navigator = this
        observers()
    }

    override fun goToHome() {
        login()
        startActivity(Intent(this,HomeActivity::class.java))
        finish()
    }

    override fun goToRegister() {
        startActivity(Intent(this,RegisterActivity::class.java))
        finish()
    }

    private fun observers(){
        viewModel.emailErrorMessage.observe(this, Observer {
            if (it==true){
                binding.email.error = "Email is not valid"
            }
        })

        viewModel.passwordErrorMessage.observe(this, Observer {
            if (it==true){
                binding.password.error = "Password is not valid"
            }
        })

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login

    }

    override fun initializeViewModel(): LoginViewModel {
        return ViewModelProvider(this).get(LoginViewModel::class.java)
    }
}