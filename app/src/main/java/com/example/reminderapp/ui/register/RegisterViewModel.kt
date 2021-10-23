package com.example.reminderapp.ui.register

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.example.reminderapp.ui.base.BaseViewModel

class RegisterViewModel: BaseViewModel<Navigator>(){
    private val TAG = "RegisterViewModel"



    val email=MutableLiveData<String>()
    val password=MutableLiveData<String>()
    val emailErrorMessage = MutableLiveData<Boolean>()
    val passwordErrorMessage = MutableLiveData<Boolean>()





    fun register(){
        Log.d(TAG, "register: ")
        Log.d(TAG, "register: ${email.value.toString()}")
        if (isDataValid()){
            Log.d(TAG, "register: dataValid")
            loader.value = true
            auth.createUserWithEmailAndPassword(email.value.toString(),password.value.toString())
                .addOnSuccessListener {
                Log.d(TAG, "register: success")
                loader.value = false
                navigator?.goToLogin()
            }.addOnFailureListener {
                Log.d(TAG, "register: failed")
                loader.value = false
                message.value = it.localizedMessage
            }

        }else{
            message.value = "Email or password is invalid"

        }
    }

    private fun isDataValid(): Boolean {
        if (email.value.isNullOrBlank()||!Patterns.EMAIL_ADDRESS.matcher(email.value!!).matches()){
            emailErrorMessage.value = true
            return false
        }else if (password.value.isNullOrBlank()){
            passwordErrorMessage.value = true
            return false
        }

        return true
    }
    fun login(){
        navigator?.goToLogin()

    }

}