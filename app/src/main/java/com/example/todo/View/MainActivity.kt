package com.example.todo.View

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.*
import com.example.todo.Adapter.Adapter
import com.example.todo.Model.Todo
import com.example.todo.Noftification.NotificationReceiver
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity()  {

    lateinit var todoViewModel: TodoViewModel
    lateinit var addTask : FloatingActionButton
    private val taskAdapter : Adapter by lazy {Adapter(this) }
    lateinit var addActivityResultLauncher : ActivityResultLauncher<Intent>
    lateinit var updateActivityResultLauncher : ActivityResultLauncher<Intent>
    var id : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addTask = findViewById(R.id.addTaskBtn)
        addTask.setOnClickListener{
            val intent = Intent(this, AddTask::class.java)
            addActivityResultLauncher.launch(intent)
        }

        val recyclerView : RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val taskAdapter = Adapter(this)
        recyclerView.adapter = taskAdapter

        registerActivityResultLauncher()




        val viewModelFactory = TodoViewModelFactory((application as TodoApplication ).repository)
        todoViewModel = ViewModelProvider(this,viewModelFactory)[TodoViewModel::class.java]
        todoViewModel.myAllTasks.observe(this){task ->
            taskAdapter.setTask(task)
        }

        ItemTouchHelper(object  : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val dialog = AlertDialog.Builder(this@MainActivity)
                dialog.setTitle("Delete Note")
                dialog.setMessage("Are you sure you want to delete Task?")
//                dialog.setIcon(@android.)
                dialog.setPositiveButton("Delete", DialogInterface.OnClickListener { dialogInterface, i ->
                    todoViewModel.delete(taskAdapter.getTask(viewHolder.adapterPosition))
                })
                dialog.setNegativeButton("Cancel" , DialogInterface.OnClickListener { dialogInterface, i ->

                    val intent = Intent(this@MainActivity,MainActivity::class.java)
                    startActivity(intent)
                    //dialogInterface.cancel()
                })

                window.setBackgroundDrawableResource(android.R.color.transparent)


                dialog.create().show()

            }

        }).attachToRecyclerView(recyclerView)
    }

    private fun registerActivityResultLauncher(){
        addActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                val resultCode = it.resultCode
                val data = it.data

                if(resultCode == RESULT_OK && data != null){
                    val taskTitle : String = data.getStringExtra("taskT").toString()
                    val taskTime : String = data.getStringExtra("taskTi").toString()
                    val taskDate : String = data.getStringExtra("taskD").toString()
                   // val taskCom : Boolean = data.getBooleanExtra("taskCom" , false)
                    val taskN = Todo(taskTitle , taskTime , taskDate )
                    todoViewModel.insert(taskN)

                }
                notify(this)



            })

        updateActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                val resultCode = it.resultCode
                val data = it.data

                if(resultCode == RESULT_OK && data != null){
                    val updatedTTitle : String = data.getStringExtra("taskT").toString()
                    val updatedTTime : String = data.getStringExtra("taskTi").toString()
                    val updatedTDate : String = data.getStringExtra("taskD").toString()
                  // val updatedTaskCom : Boolean = data.getBooleanExtra("taskC",false)
                    val taskId : Int = data.getIntExtra("taskId" , -1)

                    val newTask = Todo(updatedTTitle , updatedTTime, updatedTDate)
                    newTask.id = taskId
                    todoViewModel.insert(newTask)
                }
                notify(this)

            })


    }
    @SuppressLint("UnspecifiedImmutableFlag")
    fun notify(context : Context){
        val cal = Calendar.getInstance()
        cal.timeInMillis

        val intent = Intent(this , NotificationReceiver::class.java)

        val pendingIntent = if(Build.VERSION.SDK_INT >= 23){
            PendingIntent.getBroadcast(context , 100 , intent , PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        }
        else{
            PendingIntent.getBroadcast(context , 100 , intent , PendingIntent.FLAG_UPDATE_CURRENT)

        }

        //val time = currTask.taskTime.toLong()

        val alarmManager : AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP
            ,cal.timeInMillis, AlarmManager.INTERVAL_DAY , pendingIntent)
    }

}