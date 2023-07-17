package com.example.todo.Noftification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.todo.R
import com.example.todo.View.MainActivity

class NotificationReceiver : BroadcastReceiver() {

        private val CHANNEL_ID = "i.apps.notification"


    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context, intent : Intent) {

        val taskTitle  = intent.getStringExtra("taskTitle")

        val builder = NotificationCompat.Builder(context,CHANNEL_ID)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // Creating the channel for notification and pass the unique channel id , name and importance
            val channel = NotificationChannel(CHANNEL_ID,"1",NotificationManager.IMPORTANCE_HIGH)

            val resultIntent = Intent(context , MainActivity::class.java)
            val resultPendingIntent = PendingIntent.getActivity(context, 1 , resultIntent , PendingIntent.FLAG_UPDATE_CURRENT)
            channel.enableLights(true)
            channel.lightColor = Color.GREEN
            channel.enableVibration(false)

            // Register the channel with the system
            val manager : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
             builder.setSmallIcon(R.drawable.ic_launcher_background)
            builder.setContentTitle(taskTitle)
            builder.setContentIntent(resultPendingIntent)
        }
        else{
            builder.setSmallIcon(R.drawable.ic_launcher_background)
            builder.setContentTitle("To do")
            builder.setContentText("This is Notification")
            builder.priority = NotificationCompat.PRIORITY_HIGH
        }

        val notificationCompat = NotificationManagerCompat.from(context)
        notificationCompat.notify(1,builder.build())






    }




}