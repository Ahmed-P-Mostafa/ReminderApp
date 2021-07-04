package com.example.reminderapp.ui.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import com.example.reminderapp.ui.login.LoginActivity
import com.example.reminderapp.ui.newLecture.NewLectureActivity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * check function when add new alarm or if the device reboot if there alarms before clock reset
 * daily service for handling daily alarm manager
 * delete on long press or on swipe
 * */


class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(), Navigator{


    val adapter = LecturesAdapter(this, null)
    lateinit var lecturesList: List<Lecture>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
        binding.vm = viewModel
        lecturesList = LecturesDatabase.getInstance(this).lecturesDAO().getAllLectures()
        //viewModel.getDKMAData()


        binding.recyclerView.adapter = adapter

        viewModel.webViewLiveData.observe(this, Observer {
            if (it != "null") {
                showData(it)
            }
        })
        adapter.setOnLectureLongPressListener(object : LecturesAdapter.OnLectureLongPressListener {
            override fun onLectureLongPressListener(lecture: Lecture) {
                 showDialog(
           title = "Caution !",
           message = "Do you want to delete ${lecture.name} lecture !?",
           negBtnText = "No",
           posBtnText = "Yes",
           posBtnClickListener = DialogInterface.OnClickListener { dialog, _ ->
               LecturesDatabase.getInstance(this@HomeActivity).lecturesDAO().delete(lecture)
               lecturesList = LecturesDatabase.getInstance(this@HomeActivity).lecturesDAO().getAllLectures()
               adapter.changeData(lecturesList)
               dialog.dismiss()
           })

            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                logout()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()

            }
            R.id.deleteAll -> {
                showDialog(
                    title = "Caution!",
                    message = "Do you want to delete all the lectures ?",
                    negBtnText = "No",
                    posBtnText = "Yes",
                    posBtnClickListener = DialogInterface.OnClickListener { dialog, _ ->
                        MainScope().launch {

                            LecturesDatabase.getInstance(this@HomeActivity).lecturesDAO()
                                .deleteAll()
                            lecturesList =
                                LecturesDatabase.getInstance(this@HomeActivity).lecturesDAO()
                                    .getAllLectures()
                            adapter.changeData(lecturesList)

                        }
                    })
            }
        }


        return true
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
        val dialog = AlertDialog.Builder(this)

        dialog.setTitle("How to make my app work")
        val viewWeb: View = layoutInflater.inflate(R.layout.webview_layout, null)
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


}