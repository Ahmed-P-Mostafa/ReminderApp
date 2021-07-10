package com.example.reminderapp.pojo.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lecture")
data class Lecture(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String? = null,
    var location: String? = null,
    val code: String? = null,
    val type: String? = null,
    var time: Long? = null,
    val repeatInterval: Int = 14
)