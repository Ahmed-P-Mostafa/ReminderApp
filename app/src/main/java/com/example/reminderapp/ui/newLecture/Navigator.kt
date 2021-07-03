package com.example.reminderapp.ui.newLecture

import com.example.reminderapp.pojo.models.Lecture

interface Navigator {
    fun backToHome()
    fun insertLectureToDatabase(lecture: Lecture)
}