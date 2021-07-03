package com.example.reminderapp.pojo.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderapp.databinding.LectureItemBinding
import com.example.reminderapp.pojo.models.Lecture
import java.text.SimpleDateFormat
import java.util.*

class LecturesAdapter(var list :List<Lecture>?):RecyclerView.Adapter<LecturesAdapter.ViewHolder>() {
    private val TAG = "LecturesAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = LectureItemBinding.inflate(inflater,parent,false)

        return ViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list?.get(position)
        holder.bind(item!!)

        holder.binding.tvName.setOnClickListener {
            listener?.onLectureLongPressListener(item.id!!)
            Log.d(TAG, "onBindViewHolder: ${item.id} ")
        }
    }

    interface OnLectureLongPressListener{
        fun onLectureLongPressListener(id:Int)
    }

    var listener :OnLectureLongPressListener?=null

    fun setOnLectureLongPressListener(onLectureLongPressListener: OnLectureLongPressListener){
        this.listener = onLectureLongPressListener
    }



    override fun getItemCount() = list?.size?:0

    fun changeData(lecturesList: List<Lecture>) {
        this.list = lecturesList
        notifyDataSetChanged()

    }

    class ViewHolder(val binding:LectureItemBinding) :RecyclerView.ViewHolder(binding.root){


        fun bind(item :Lecture){
            val dayFormat = SimpleDateFormat("EEE")
            val hoursFormat = SimpleDateFormat("HH:mm")

            val date = Calendar.getInstance()
            date.timeInMillis = item.time?:42115355L


            val day = dayFormat.format(date.time)
            val hour = hoursFormat.format(date.time)
            binding.lecture = item
            binding.tvDay.text = day
            binding.tvTime.text = hour
            binding.invalidateAll()

        }

    }
}