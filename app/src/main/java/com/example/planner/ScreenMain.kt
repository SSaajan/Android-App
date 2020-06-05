package com.example.planner

import android.app.ActionBar
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planner.database.ReminderDatabase
import com.example.planner.databinding.FragmentScreenMainBinding
import kotlinx.coroutines.*

class ScreenMain() : Fragment() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        (activity as MainActivity).supportActionBar?.title ="Planner"

        val binding = DataBindingUtil.inflate<FragmentScreenMainBinding>(
            inflater,
            R.layout.fragment_screen_main, container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = ReminderDatabase.getInstance(application).reminderDatabasedao

        binding.innerLayout.visibility = View.GONE

        val reminders = dataSource.getAllReminders()

        val adapter = RecyclerAdapter(dataSource)
        binding.Reminders.adapter = adapter
        binding.Reminders.layoutManager = LinearLayoutManager(activity)

        reminders.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })

        binding.innerLayout.setOnClickListener {
            binding.innerLayout.visibility = View.GONE
        }

        suspend fun clear() {
            withContext(Dispatchers.IO) {
                dataSource.clear()
            }
        }

        binding.clearFab.setOnClickListener {
            uiScope.launch {
                clear()
            }
        }

        binding.addFab.setOnClickListener {
            view?.findNavController()
                ?.navigate(ScreenMainDirections.actionScreenMainToAddReminder())
        }

        binding.fab.setOnClickListener {
            binding.innerLayout.visibility = View.VISIBLE
        }

        binding.lifecycleOwner = this

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,
            view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

}
