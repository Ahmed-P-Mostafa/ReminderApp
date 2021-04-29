package com.example.reminderapp.ui.base

import android.app.ProgressDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.reminderapp.R
import com.example.reminderapp.ui.register.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import java.lang.reflect.Type

abstract class BaseActivity<DB:ViewDataBinding,VM:BaseViewModel<*>> : AppCompatActivity() {
    private val TAG = "BaseActivity"
    lateinit var viewModel: VM
    lateinit var binding:DB
    private var loader:ProgressDialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,getLayoutId())
        viewModel = initializeViewModel()
        observers()
    }
    abstract fun getLayoutId():Int
    abstract fun initializeViewModel():VM


    fun showMessage(message: String){
        AlertDialog.Builder(this).setMessage(message).setCancelable(false).setPositiveButton("Ok",DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        }).show()
    }
    fun showLoader(){
        loader = ProgressDialog(this)
        loader?.apply { ->
            setMessage("Loading...")
            setCancelable(false)
            show()
        }

    }
    fun hideLoader(){
        if (loader!=null &&loader?.isShowing!!){
            loader?.dismiss()
        }
    }


    private fun observers(){
        viewModel.message.observe(this, Observer {
            showMessage(it)
        })
        viewModel.loader.observe(this, Observer {
            when(it){
                true -> showLoader()
                false -> hideLoader()
            }
        })

    }
}