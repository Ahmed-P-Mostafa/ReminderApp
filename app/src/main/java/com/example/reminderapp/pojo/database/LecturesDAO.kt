package com.example.reminderapp.pojo.database

import androidx.room.*
import com.example.reminderapp.pojo.models.Lecture

@Dao
interface LecturesDAO {

    @Insert
    fun insert(lecture: Lecture) :Long

    @Update
    fun update(lecture: Lecture)

    @Delete
    fun delete(lecture: Lecture)

    @Query("Select * from Lecture")
    fun getAllLectures():List<Lecture>

    @Query("select * from lecture where id like :id")
    fun getLecture(id:Int):Lecture

    @Query("DELETE FROM LECTURE")
    fun deleteAll()
}