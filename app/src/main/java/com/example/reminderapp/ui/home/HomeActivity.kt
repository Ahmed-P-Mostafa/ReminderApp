package com.example.reminderapp.ui.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.reminderapp.R
import com.example.reminderapp.databinding.ActivityHomeBinding
import com.example.reminderapp.pojo.adapters.LecturesAdapter
import com.example.reminderapp.pojo.database.LecturesDatabase
import com.example.reminderapp.pojo.models.Lecture
import com.example.reminderapp.ui.base.BaseActivity
import com.example.reminderapp.ui.newLecture.NewLectureActivity

/**
 * check function when add new alarm or if the device reboot if there alarms before clock reset
 * daily service for handling daily alarm manager
 * delete on long press or on swipe
 * */


class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(),Navigator,LecturesAdapter.OnLectureLongPressListener {


    val adapter = LecturesAdapter(null)
    lateinit var lecturesList :List<Lecture>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
        binding.vm = viewModel
        lecturesList = LecturesDatabase.getInstance(this).lecturesDAO().getAllLectures()
        //viewModel.getDKMAData()


        binding.recyclerView.adapter = adapter

        viewModel.webViewLiveData.observe(this, Observer {
            if (it!="null"){
                showData(it)
            }
        })




    }


    override fun onStart() {
        super.onStart()

        adapter.list = lecturesList
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun initializeViewModel(): HomeViewModel {
        return ViewModelProvider(this).get(HomeViewModel::class.java)

    }

    override fun gotoNewLecture() {
        startActivity(Intent(this, NewLectureActivity::class.java))

    }

    // Use this method in your UI, Activity or Fragment
    fun showData(result: String) {
       val dialog =  AlertDialog.Builder(this)

        dialog.setTitle("How to make my app work")
        val viewWeb : View = layoutInflater.inflate(R.layout.webview_layout,null)
        val webView = findViewById<WebView>(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.loadUrl(result)
        dialog.setView(viewWeb)


        dialog.setPositiveButton(android.R.string.ok) { dialog, which ->
            dialog.dismiss()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        dialog.show()
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

    override fun onLectureLongPressListener(id: Int) {

        val lecture = LecturesDatabase.getInstance(this).lecturesDAO().getLecture(id)

        Toast.makeText(this, lecture.name, Toast.LENGTH_SHORT).show()

    }
}