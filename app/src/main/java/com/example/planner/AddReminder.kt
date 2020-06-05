package com.example.planner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.planner.database.ReminderClass
import com.example.planner.database.ReminderDatabase
import com.example.planner.databinding.FragmentAddReminderBinding
import kotlinx.coroutines.*
import java.util.*

class AddReminder() : Fragment() {

    lateinit var reminder: String
    var deadline: String = "Select the deadline"
    var priority: Int = 1
    var priorityFlag: Int = 0

    private var addremJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + addremJob)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        (activity as MainActivity).supportActionBar?.title ="Add Reminder"

        val binding = DataBindingUtil.inflate<FragmentAddReminderBinding>(
            inflater,
            R.layout.fragment_add_reminder,
            container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = ReminderDatabase.getInstance(application).reminderDatabasedao

        binding.DeadlineText.text = deadline
        binding.calendarView.visibility = View.GONE
        binding.priorityText.visibility = View.GONE

        binding.DeadlineText.setOnClickListener {
            binding.calendarView.visibility = View.VISIBLE
            binding.calendarView.setOnDateChangeListener(
                CalendarView.OnDateChangeListener { _, year: Int, month: Int, day: Int ->
                    var d: String = day.toString()
                    if(day < 10){
                        d = "0$day"
                    }
                    var m = month
                    m++
                    var temp: String = m.toString()
                    if((month + 1) < 10){
                        temp = "0$m"
                    }
                    deadline = "$d/$temp/$year"
                    print(deadline)
                    binding.DeadlineText.text = deadline
                    /*
                    Toast.makeText(context, "Deadline Given: $deadline, day: $day, " +
                            "month: $month, year:$year", Toast.LENGTH_LONG).show()*/
                })
        }

        binding.addScreenLayout.setOnClickListener {
            binding.calendarView.visibility = View.GONE
        }

        binding.prioritySwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.priorityText.visibility = View.VISIBLE
            } else {
                binding.priorityText.visibility = View.GONE
            }
        }

        binding.saveButton.setOnClickListener {
            deadline = binding.DeadlineText.text.toString()
            reminder = binding.reminderText.text.toString()
            if (binding.prioritySwitch.isChecked) {
                priority = binding.priorityText.text.toString().toInt()
                priorityFlag = 1
            } else {
                priorityFlag = 0
            }
            if (priorityFlag == 0) {
                priority = prioritySetter(deadline)
            }
            suspend fun insert(reminder: ReminderClass) {
                withContext(Dispatchers.IO) {
                    dataSource.insert(reminder)
                }
            }
            view?.findNavController()?.navigate(R.id.action_addReminder_to_screenMain)
            uiScope.launch {
                val newRem = ReminderClass(reminder, deadline, priority, priorityFlag)
                insert(newRem)
            }

        }

        binding.clearButton.setOnClickListener {
            binding.DeadlineText.setText("Select the deadline")
            binding.reminderText.setText("")
            binding.DeadlineText.visibility = View.VISIBLE
            binding.calendarView.visibility = View.GONE
            binding.prioritySwitch.setChecked(false)
        }

        return binding.root
    }

    private fun prioritySetter(deadline: String): Int {
        val c = Calendar.getInstance()
        val Cyear: Int = c.get(Calendar.YEAR)
        val Cmonth: Int = c.get(Calendar.MONTH) + 1
        val Cday: Int = c.get(Calendar.DAY_OF_MONTH)

        val day: Int = deadline.substring(0, 2).toInt()
        val month: Int = deadline.substring(3, 5).toInt()
        val year: Int = deadline.substring(6).toInt()
        var x = day - Cday
        Toast.makeText(context, "Current : $Cday/$Cmonth/$Cyear" +
                "Given: $day/$month/$year" +
                "Diff: $x", Toast.LENGTH_LONG).show()

        if (year == Cyear && Cmonth == month) {
            priority = when (day - Cday) {
                0 -> 5
                1 -> 5
                2 -> 4
                3 -> 3
                4 -> 2
                else -> 1
            }
        }
        return priority
    }


}
