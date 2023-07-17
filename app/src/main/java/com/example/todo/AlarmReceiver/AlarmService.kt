package com.example.todo.AlarmReceiver

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import com.example.todo.Noftification.NotificationReceiver

class AlarmService(private val context: Context) {
    private val alarmManager : AlarmManager? =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

    fun setExactAlarm(timeInMillis : Long , taskT : String) = setAlarm(
        timeInMillis,
        getPendingIntent (
            getIntent().apply {
                putExtra("taskTitle", taskT)
            }
        )
    )

    fun updateAlarm(timeInMillis : Long , taskT : String) {
        stopAlarm(
            getPendingIntent(
                getIntent()
            )
        )
        setAlarm(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    putExtra("taskTitle", taskT)
                }
            )
        )
    }

    fun stopAlarm(pendingIntent: PendingIntent)
    {
        getPendingIntent(
            getIntent().apply {
                alarmManager?.cancel(pendingIntent)
            }
        )

    }

    private fun setAlarm(timeInMillis: Long,pendingIntent: PendingIntent){

        alarmManager?.let {
            if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
                if(System.currentTimeMillis() < timeInMillis){
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        timeInMillis,
                        pendingIntent
                    )
                }

            }else{
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            }
        }
    }

    private fun getIntent() = Intent(context, NotificationReceiver::class.java)

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getPendingIntent(intent:  Intent) =
        PendingIntent.getBroadcast(
            context,
            200,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
            )
}



private fun AlarmManager?.cancel(intent: Intent) {

}
