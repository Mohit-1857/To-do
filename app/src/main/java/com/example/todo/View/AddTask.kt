package com.example.todo.View

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.AlarmReceiver.AlarmService
import com.example.todo.R
import java.util.*

class AddTask : AppCompatActivity() , TimePickerDialog.OnTimeSetListener , DatePickerDialog.OnDateSetListener {

    lateinit var editTask : EditText
    lateinit var btnTimeDate : Button
    lateinit var txtDate : TextView
    lateinit var txtTime : TextView
    lateinit var btnAddTask : Button
    lateinit var btnCancel : Button
    lateinit var alarmService: AlarmService

    private val context : Context
        get() {
            TODO()
        }



    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onCreate(
        savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_task)

        supportActionBar?.hide()


        alarmService = AlarmService(this)
        editTask = findViewById(R.id.editTask)
        btnTimeDate = findViewById(R.id.btnTimeDate)
        txtDate = findViewById(R.id.txtDate)
        txtTime = findViewById(R.id.txtTime)
        btnAddTask = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)

        btnTimeDate.setOnClickListener {
            val taskT = editTask.text.toString()
            if(!taskT.isEmpty()){
                notifyTask{  timeInMillis ->  alarmService.setExactAlarm(timeInMillis , taskT)}
            }


        }

//




        btnAddTask.setOnClickListener {
            saveTask()

        }

        btnCancel.setOnClickListener{
            finish()
        }


    }

    fun saveTask(){
        val taskTitle = editTask.text.toString()
        val taskTime = txtTime.text.toString()
        val taskDate = txtDate.text.toString()



        if(taskTitle.isEmpty() && taskTime.isEmpty() && taskDate.isEmpty())  {
            Toast.makeText(this,"Empty task",Toast.LENGTH_LONG).show()
        }
        else{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("taskT" , taskTitle)
            intent.putExtra("taskTi", taskTime)
            intent.putExtra("taskD", taskDate)
            setResult(RESULT_OK,intent)
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
       txtTime.text = ("$hour:$minute")
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        txtDate.text = ("$dayOfMonth/$month/$year")
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun notifyTask(callback: (Long) -> Unit){

        Calendar.getInstance().apply {
            DatePickerDialog(this@AddTask,
            0,
                {
                    _,year , month,day ->
                    this.set(Calendar.YEAR,year)
                    this.set(Calendar.MONTH,month)
                    this.set(Calendar.DAY_OF_MONTH,day)
                    txtDate.text = "$day/$month/$year"

                    TimePickerDialog(
                    this@AddTask,
                    0,
                    { _ , hour , minute ->
                        this.set(Calendar.HOUR_OF_DAY , hour)
                        this.set(Calendar.MINUTE , minute)
                        txtTime.text = "$hour : $minute"
                        callback(this.timeInMillis)
                    },
                       this.get(Calendar.HOUR_OF_DAY),
                       this.get(Calendar.MINUTE),
                        false
                    ).show()
                },
            this.get(Calendar.YEAR),
            this.get(Calendar.MONTH),
            this.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

    }
}