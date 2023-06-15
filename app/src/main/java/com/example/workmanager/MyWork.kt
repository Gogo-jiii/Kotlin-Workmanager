package com.example.workmanager

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWork(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val handler: Handler = Handler(Looper.getMainLooper())
    private val context: Context
    private var progress = 10

    companion object {
        var isStopped: Boolean = false
    }

    init {
        this.context = context
        setProgressAsync(Data.Builder().putInt("PROGRESS", 0).build())
        Companion.isStopped = false
    }

    override fun doWork(): Result {
        val inputData = inputData.getString("key")
        handler.post {
            Toast.makeText(
                context, "one time work inputData: $inputData",
                Toast.LENGTH_SHORT
            ).show()
        }
        for (i in 1..5) {
            if (Companion.isStopped) {
                break
            }
            progress = i * 100 / 100
            handler.post { //ui task
                Log.d("TAG", i.toString())
                Toast.makeText(context, "One time work data: $i", Toast.LENGTH_LONG).show()
            }
            try {
                setProgressAsync(Data.Builder().putInt("PROGRESS", progress).build())
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        //output data
        val outputData = Data.Builder().putString("output", "Some_Output_Data").build()
        return Result.success(outputData)
    }
}