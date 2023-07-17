package com.example.todo.View

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.AlarmReceiver.AlarmService
import com.example.todo.R
import java.util.*


class UpdateTask  : AppCompatActivity() ,TimePickerDialog.OnTimeSetListener , DatePickerDialog.OnDateSetListener {
    lateinit var editTaskU : EditText
    lateinit var btnTimeUp : TextView
    lateinit var btnTimeDateUp : Button
    lateinit var btnDateUp : TextView
    lateinit var btnUpdateTask : Button
    lateinit var btnCancelU : Button
    private val context : Context
        get() {
            TODO()
        }
    var currId = -1

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(

        savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.updatetask)

        supportActionBar?.hide()

        editTaskU = findViewById(R.id.updateTask)
        btnTimeUp = findViewById(R.id.taskTimeU)
        btnDateUp = findViewById(R.id.taskDateU)
        btnTimeDateUp = findViewById(R.id.btnTimeDate)
        btnUpdateTask = findViewById(R.id.btnUpdate)
        btnCancelU = findViewById(R.id.btnCancelU)
        lateinit var alarmService: AlarmService

        getAndSetTask()

        btnTimeDateUp.setOnClickListener {

//            val intent = Intent(this, NotificationReceiver::class.java)
//            val pendingIntent = PendingIntent.getBroadcast(applicationContext, 1253, intent, 0)
//            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//            alarmManager.cancel(pendingIntent)

            val taskT = editTaskU.text.toString()
            if(taskT.isNotEmpty()){
                notifyTask{
                        timeInMillis ->  alarmService.updateAlarm(timeInMillis , taskT)
                }
            }
        }


        btnUpdateTask.setOnClickListener {

            updateTask()
        }

        btnCancelU.setOnClickListener{
            finish()
        }


    }

    fun updateTask(){
        val updatedTTitle = editTaskU.text.toString()
        val updatedTTime = btnTimeUp.text.toString()
        val updatedTDate = btnDateUp.text.toString()


        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("taskT" , updatedTTitle)
        intent.putExtra("taskTi", updatedTTime)
        intent.putExtra("taskD", updatedTDate)
        //intent.putExtra("taskId",currId)

        if(updatedTTitle.isEmpty()  && updatedTTime.isEmpty() && updatedTDate.isEmpty()){
            Toast.makeText(this,"Empty task", Toast.LENGTH_LONG).show()
        }
        else{
            if(currId != -1){
                Toast.makeText(this,"Task is : $updatedTTitle", Toast.LENGTH_LONG).show()
                setResult(RESULT_OK,intent)
                finish()
            }

        }
    }

    fun getAndSetTask(){
        val currTTitle = intent.getStringExtra("currTask")
        val currTTime = intent.getStringExtra("currTaskTi")
        val currTDate = intent.getStringExtra("currTaskD")
        currId = intent.getIntExtra("currTaskId",-1)
        editTaskU.setText(currTTitle)
        btnTimeUp.text = currTTime
        btnDateUp.text =  currTDate
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        btnTimeUp.text = ("$hour:$minute")
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        btnDateUp.text = ("$dayOfMonth/$month/$year")
    }

    @SuppressLint("SetTextI18n")
    private fun notifyTask(callback: (Long) -> Unit){

        Calendar.getInstance().apply {
            DatePickerDialog(this@UpdateTask,
                0,
                {
                        _,year , month,day ->
                    this.set(Calendar.YEAR,year)
                    this.set(Calendar.MONTH,month)
                    this.set(Calendar.DAY_OF_MONTH,day)
                    btnDateUp.text = "$day/$month/$year"

                    TimePickerDialog(
                        this@UpdateTask,
                        0,
                        { _ , hour , minute ->
                            this.set(Calendar.HOUR_OF_DAY , hour)
                            this.set(Calendar.MINUTE , minute)
                            btnDateUp.text = "$hour : $minute"
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