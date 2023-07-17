package com.example.todo.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.View.MainActivity
import com.example.todo.R
import com.example.todo.Model.Todo
import com.example.todo.View.UpdateTask
import kotlin.collections.ArrayList

class Adapter(private val activity: MainActivity) : RecyclerView.Adapter<Adapter.TaskViewHolder>() {
   var task : List<Todo> = ArrayList()


    class TaskViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val taskTxt : TextView = itemView.findViewById(R.id.task)
        val taskCom : CheckBox = itemView.findViewById(R.id.taskCheck)
        val taskTime : TextView = itemView.findViewById(R.id.taskTime)
        val taskDate : TextView = itemView.findViewById(R.id.taskCDate)
        val cardView : CardView = itemView.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
       val view : View = LayoutInflater.from(parent.context).inflate(R.layout.task,parent,false)
        return TaskViewHolder(view)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        var currTask : Todo = task[position]
        holder.taskTxt.text = currTask.taskTitle
        holder.taskTime.text = currTask.taskTime
        holder.taskDate.text = currTask.taskDate


        holder.cardView.setOnClickListener{
            val intent = Intent(activity, UpdateTask::class.java)
            intent.putExtra("currTask" , currTask.taskTitle)
            intent.putExtra("currTaskTi", currTask.taskTime)
            intent.putExtra("currTaskD", currTask.taskDate)
            intent.putExtra("currTaskId",currTask.id)
            activity.updateActivityResultLauncher.launch(intent)
        }

        //setTask(task)



//        val cal = Calendar.getInstance()
//        cal.timeInMillis
//
//        val intent = Intent(activity , NotificationReceiver::class.java)
//
//        val pendingIntent = if(Build.VERSION.SDK_INT >= 23){
//            PendingIntent.getBroadcast(activity , 100 , intent , PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
//
//        }
//        else{
//            PendingIntent.getBroadcast(activity , 100 , intent , PendingIntent.FLAG_UPDATE_CURRENT)
//
//        }
//
//        //val time = currTask.taskTime.toLong()
//
//        val alarmManager : AlarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP
//        ,cal.timeInMillis, AlarmManager.INTERVAL_DAY , pendingIntent)



        holder.taskCom.setOnClickListener{
            activity.todoViewModel.delete(currTask)
            val intent = Intent(activity,MainActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }


//        holder.taskCom.setOnCheckedChangeListener { _, isChecked ->
//            if(isChecked){
//                val intent = Intent(activity,MainActivity::class.java)
////                intent.putExtra("cTask" , currTask.taskTitle)
////                intent.putExtra("cTaskTi", currTask.taskTime)
////                intent.putExtra("cTaskD", currTask.taskDate)
////                intent.putExtra("cTaskId",currTask.id)
//                activity.startActivity(intent)
//                //activity.taskCompletedResultLauncher.launch(intent)
//
//
//
//                //activity.setResult(RESULT_OK,intent)
//                //activity.finish()
//            }
//
//        }
    }

    override fun getItemCount(): Int {

        return task.size
    }


    @SuppressLint("NotifyDataSetChanged")
    @JvmName("setTask1")
    fun setTask(myTasks : List<Todo>){
        this.task = myTasks
        notifyDataSetChanged()
    }

    fun getTask(position: Int) : Todo {
        return task[position]
    }
}