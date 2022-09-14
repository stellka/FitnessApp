package com.steluxa.fitnessapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.steluxa.fitnessapp.R
import com.steluxa.fitnessapp.adapter.DayModel
import com.steluxa.fitnessapp.adapter.DaysAdapter
import com.steluxa.fitnessapp.adapter.ExerciseAdapter
import com.steluxa.fitnessapp.databinding.ExerciseListFragmentBinding
import com.steluxa.fitnessapp.databinding.FragmentDaysBinding
import com.steluxa.fitnessapp.utils.FragmentManager
import com.steluxa.fitnessapp.utils.MainViewModel

class ExerciseListFragment : Fragment() {
    private lateinit var binding: ExerciseListFragmentBinding
    private val model: MainViewModel by activityViewModels()
    private lateinit var exerciseAdapter: ExerciseAdapter
    private var actionBar: ActionBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExerciseListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        model.mutableListExercise.observe(viewLifecycleOwner){
            for (i in 0 until model.getExerciseCount()){
                it[i] = it[i].copy(isDone = true)
            }
            exerciseAdapter.submitList(it)
        }
    }

    private fun init() = with(binding){
        actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.exercises_2)
        exerciseAdapter = ExerciseAdapter()
        rcView.layoutManager = LinearLayoutManager(activity)
        rcView.adapter = exerciseAdapter
        bStart.setOnClickListener {
            FragmentManager.setFragment(WaitingFragment.newInstance(), activity as AppCompatActivity)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ExerciseListFragment()
    }
}