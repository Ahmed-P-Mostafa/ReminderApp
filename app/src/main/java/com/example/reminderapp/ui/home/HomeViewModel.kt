package com.example.reminderapp.ui.home

import android.app.AlertDialog
import android.os.Build
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.reminderapp.R
import com.example.reminderapp.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL
import java.util.*

class HomeViewModel:BaseViewModel<Navigator>() {
    private val TAG = "HomeViewModel"

    var webViewLiveData = MutableLiveData<String>()

    fun goToNewLecture(){
        navigator?.gotoNewLecture()
    }

    // Use this method in your ViewModel
    fun getDKMAData() {

        viewModelScope.launch(Dispatchers.IO) {

            val result = try {
                val manufacturer = Build.MANUFACTURER.toLowerCase(Locale.ROOT).replace(" ", "-")
                val url = URL("https://dontkillmyapp.com/api/v2/$manufacturer.json")
                val json = JSONTokener(url.readText()).nextValue() as JSONObject?
                json?.getString("user_solution")?.replace(Regex("\\[[Yy]our app\\]"), R.string.app_name.toString())
            } catch (e: Exception) {
                // Vendor not present in the DontKillMyApp list
                null
            }
            Log.d(TAG, "getDKMAData: $result")


            withContext(Dispatchers.Main) {
                Log.d(TAG, "getDKMAData: $result")
                when (result) {
                    null -> {message.value = "Please make sure to open app settings and allow auto start and battery optimization."}
                    else ->{ webViewLiveData.value = result.toString()}

                }
            }

        }
    }

    fun WebView.loadDKMAData(result: String) {

        loadData(result, "text/html; charset=utf-8", "UTF-8")
        webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                loadUrl(url)
                return true
            }
        }
    }


}