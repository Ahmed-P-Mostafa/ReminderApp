package com.example.reminderapp.di

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