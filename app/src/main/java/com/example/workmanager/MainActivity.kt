package com.example.workmanager

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.Operation
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.workmanager.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var oneTimeWorkRequest: OneTimeWorkRequest
    private lateinit var context: Context
    private lateinit var constraints: Constraints

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        this.context = this

        binding.btnStartWork.setOnClickListener {
            Toast.makeText(context, "Starting one time work!", Toast.LENGTH_SHORT).show()

            val data: Data = Data.Builder().putString("key", "IT wala").build()

            constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
//                .setRequiresCharging(false)
//                .setRequiresStorageNotLow(true)
//                .setRequiresBatteryNotLow(true)

            oneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWork::class.java)
                .addTag("onetimework" + System.currentTimeMillis())
                .setConstraints(constraints)
                .setInputData(data)
                .setInitialDelay(1, TimeUnit.SECONDS)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    WorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.SECONDS
                )
                .build()
            WorkManager.getInstance(context).enqueue(oneTimeWorkRequest)
        }

        binding.btnStopWork.setOnClickListener {
            val operation: Operation =
                WorkManager.getInstance(context).cancelWorkById(oneTimeWorkRequest.id)
            MyWork.isStopped = true
            Toast.makeText(
                context, "Stopping one time work!" + operation.state,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}