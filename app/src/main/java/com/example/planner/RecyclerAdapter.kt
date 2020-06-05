package com.example.planner

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.planner.database.ReminderClass
import com.example.planner.database.ReminderDatabaseDAO
import kotlinx.android.synthetic.main.item_layout.view.*
import kotlinx.coroutines.*


class RecyclerAdapter(val datasource: ReminderDatabaseDAO):
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    var data = listOf<ReminderClass>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    class ViewHolder(val itemview: View): RecyclerView.ViewHolder(itemview)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    var color: Int = Color.parseColor("#e03532")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.itemview.item.text = item.reminderText
        holder.itemview.date.text = item.Rdeadline
        if("Party" in item.reminderText){
            holder.itemview.imageView.setImageResource(R.drawable.ic_party)
        }
        else if("Exam" in item.reminderText){
            holder.itemview.imageView.setImageResource(R.drawable.ic_exam)
        }
        else if("Assignment" in item.reminderText){
            holder.itemview.imageView.setImageResource(R.drawable.ic_project)
        }
        else{
            holder.itemview.imageView.setImageResource(R.drawable.ic_default)
        }
        var bg: Int = R.drawable.reditem
        if(item.Rpriority >= 4) {
            var bg: Int = R.drawable.reditem
        }
        else if(item.Rpriority < 4 && item.Rpriority >=2){
            bg = R.drawable.orangeitem
        }
        else{
            bg = R.drawable.greenitem
        }
        holder.itemview.layout.setBackgroundResource(bg)

        suspend fun Delete(s: String) {
            withContext(Dispatchers.IO) {
                datasource.delete(s)
            }
        }

        holder.itemview.menuButton.setOnClickListener{ //creating a popup menu

            val popup: PopupMenu = PopupMenu(it.context, holder.itemview.menuButton)
            popup.inflate(R.menu.remoptions)
            //adding click listener
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.delete -> {
                        uiScope.launch {
                            Delete(data[position].reminderText)
                        }
                    }
                }
                false
            }
            //displaying the popup
            popup.show()
        }

    }
}