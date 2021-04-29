package com.example.reminderapp.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

open class BaseViewModel<N>:ViewModel() {
    val auth = FirebaseAuth.getInstance()
    val message=MutableLiveData<String>()
    val loader = MutableLiveData<Boolean>()
    var navigator :N?=null

}