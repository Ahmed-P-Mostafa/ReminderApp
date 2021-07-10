package com.example.reminderapp.pojo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.reminderapp.pojo.models.Lecture

@Database(entities = arrayOf(Lecture::class),version = 4,exportSchema = false)
abstract class LecturesDatabase :RoomDatabase() {

    abstract fun lecturesDAO():LecturesDAO

    companion object{
        const val databaseName = "LECTURES_DATABASE"
        private var db :LecturesDatabase?=null

        @Synchronized
        fun getInstance(context: Context):LecturesDatabase{
            if (db==null){
                db = Room.databaseBuilder(context,LecturesDatabase::class.java, databaseName)
                    .fallbackToDestructiveMigration().allowMainThreadQueries().build()
            }
            return db as LecturesDatabase

        }

    }
}