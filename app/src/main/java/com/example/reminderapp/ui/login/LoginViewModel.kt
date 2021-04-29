package com.example.reminderapp.ui.login

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.example.reminderapp.ui.base.BaseViewModel

class LoginViewModel :BaseViewModel<Navigator>() {

    val email= MutableLiveData<String>()
    val password= MutableLiveData<String>()
    val emailErrorMessage = MutableLiveData<Boolean>(false)
    val passwordErrorMessage = MutableLiveData<Boolean>(false)

    fun login(){
        if (isDataValid()) {
            loader.value = true
            auth.signInWithEmailAndPassword(email.value.toString(), password.value.toString())
                .addOnSuccessListener {
                    loader.value = false
                    navigator?.goToHome()

                }.addOnFailureListener {
                    loader.value = false
                    message.value = it.localizedMessage
            }
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
    fun register(){
        navigator?.goToRegister()
    }


}