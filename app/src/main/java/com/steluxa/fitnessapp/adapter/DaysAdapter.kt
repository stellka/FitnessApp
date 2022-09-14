package com.steluxa.fitnessapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.steluxa.fitnessapp.R
import com.steluxa.fitnessapp.databinding.DaysListItemBinding
import com.steluxa.fitnessapp.fragments.ExerciseListFragment
import com.steluxa.fitnessapp.utils.FragmentManager

class DaysAdapter(var listener: Listener) : ListAdapter<DayModel, DaysAdapter.DayHolder>(MyComparator()) {

    class DayHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = DaysListItemBinding.bind(view)

        fun setData(day: DayModel, listener: Listener) = with(binding){
            val name = root.context.getString(R.string.day) + " " + "${adapterPosition + 1}"
            textViewName.text = name
            val exerciseCounter = day.exercises.split(",").size.toString() + " " + root.context.getString(R.string.exercises)
            textViewCounter.text = exerciseCounter
            checkBox.isChecked = day.isDone
            itemView.setOnClickListener {
                listener.onClick(day.copy(dayNumber = adapterPosition + 1))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.days_list_item, parent, false)
        return DayHolder(view)
    }

    override fun onBindViewHolder(holder: DayHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }

    class MyComparator: DiffUtil.ItemCallback<DayModel>(){
        override fun areItemsTheSame(oldItem: DayModel, newItem: DayModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DayModel, newItem: DayModel): Boolean {
            return oldItem == newItem
        }
    }
    interface Listener{
        fun onClick(day: DayModel)
    }
}