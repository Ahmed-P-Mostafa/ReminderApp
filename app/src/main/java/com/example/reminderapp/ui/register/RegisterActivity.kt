package com.example.reminderapp.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.reminderapp.R
import com.example.reminderapp.databinding.ActivityRegisterBinding
import com.example.reminderapp.ui.base.BaseActivity
import com.example.reminderapp.ui.login.LoginActivity

class RegisterActivity : BaseActivity<ActivityRegisterBinding,RegisterViewModel>(), Navigator {

    private val TAG = "RegisterActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.model = viewModel
        viewModel.navigator = this
        observers()
        binding.email.addTextChangedListener {
            Log.d(TAG, "onCreate: watcher = ${it.toString()}")
        }

    }

    override fun goToLogin() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
    private fun observers(){
        viewModel.emailErrorMessage.observe(this, Observer {
            if (it==true){
                binding.email.error = "Email is not valid"
            }
        })

        viewModel.passwordErrorMessage.observe(this, Observer {

                binding.password.error = "Password is not valid"

        })
       /* viewModel.email.observe(this, Observer {
            Log.d(TAG, "observers: $it")
        })
*/
    }

    override fun getLayoutId(): Int {

        return R.layout.activity_register
    }

    override fun initializeViewModel(): RegisterViewModel {
      return ViewModelProvider(this).get(RegisterViewModel::class.java)
    }
}