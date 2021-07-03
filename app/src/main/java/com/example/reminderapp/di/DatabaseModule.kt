package com.example.reminderapp.di

import android.content.Context
import androidx.room.Room
import com.example.reminderapp.pojo.database.LecturesDAO
import com.example.reminderapp.pojo.database.LecturesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

object DatabaseModule {


   /* @Singleton
    @Provides
    fun provideDatabaseInstance(@ApplicationContext context: Context):LecturesDatabase{
        return Room.databaseBuilder(context,LecturesDatabase::class.java,"LECTURES_DATABASE").allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideLectureDAO(lecturesDatabase: LecturesDatabase):LecturesDAO{
        return lecturesDatabase.lecturesDAO()
    }*/
}