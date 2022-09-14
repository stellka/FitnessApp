package com.steluxa.fitnessapp.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.steluxa.fitnessapp.R
import com.steluxa.fitnessapp.utils.TimeUtils
import com.steluxa.fitnessapp.adapter.ExerciseModel
import com.steluxa.fitnessapp.databinding.ExerciseBinding
import com.steluxa.fitnessapp.utils.FragmentManager
import com.steluxa.fitnessapp.utils.MainViewModel
import pl.droidsonroids.gif.GifDrawable

class ExerciseFragment : Fragment() {
    private lateinit var binding: ExerciseBinding
    private var exerciseCounter = 0
    private var exList: ArrayList<ExerciseModel>? = null
    private var timer: CountDownTimer? = null
    private val model: MainViewModel by activityViewModels()
    private var actionBar: ActionBar? = null
    private var currentDay = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentDay = model.currentDay
        exerciseCounter = model.getExerciseCount()
        actionBar = (activity as AppCompatActivity).supportActionBar
        model.mutableListExercise.observe(viewLifecycleOwner) {
            exList = it
            nextExercise()
        }
        binding.bNext.setOnClickListener {
            nextExercise()
        }
    }

    private fun nextExercise() {
        if (exerciseCounter < exList?.size!!) {
            val ex = exList?.get(exerciseCounter++) ?: return
            showExercise(ex)
            setExerciseType(ex)
            showNextExercise()
        } else {
            exerciseCounter++
            FragmentManager.setFragment(
                DayFinishFragment.newInstance(),
                activity as AppCompatActivity
            )
        }
    }


    private fun showExercise(exercise: ExerciseModel) = with(binding) {
        ivMain.setImageDrawable(GifDrawable(root.context.assets, exercise.image))
        tvName.text = exercise.name
        val title = "$exerciseCounter / ${exList?.size}"
        actionBar?.title = title
    }

    private fun setExerciseType(exercise: ExerciseModel) {
        if (exercise.time.startsWith("x")) {
            binding.progressBar2.max = 10
            binding.progressBar2.progress = 10
            binding.tvTime.text = exercise.time
            timer?.cancel()
        } else {
            startTimer(exercise)
        }
    }

    private fun showNextExercise() = with(binding) {
        if (exerciseCounter < exList?.size!!) {
            val ex = exList?.get(exerciseCounter) ?: return
            ivNext.setImageDrawable(GifDrawable(root.context.assets, ex.image))
            getTimeType(ex)
        } else {
            ivNext.setImageDrawable(GifDrawable(root.context.assets, "congrats.gif"))
            tvNextName.text = getString(R.string.done)
        }
    }

    private fun getTimeType(ex: ExerciseModel) {
        if (ex.time.startsWith("x")) {
            binding.tvNextName.text = ex.time

        } else {
            val name = ex.name + ": ${TimeUtils.getTime(ex.time.toLong() * 1000)}"
            binding.tvNextName.text = name
        }
    }

    private fun startTimer(exercise: ExerciseModel) = with(binding) {
        progressBar2.max = exercise.time.toInt() * 1000
        timer?.cancel()
        timer = object : CountDownTimer(exercise.time.toLong() * 1000, 1) {
            override fun onTick(restTime: Long) {
                tvTime.text = TimeUtils.getTime(restTime)
                progressBar2.progress = restTime.toInt()
            }

            override fun onFinish() {
                nextExercise()
            }
        }.start()
    }

    override fun onDetach() {
        super.onDetach()
        model.savePref(currentDay.toString(), exerciseCounter - 1)
        timer?.cancel()
    }


    companion object {
        @JvmStatic
        fun newInstance() = ExerciseFragment()
    }
}