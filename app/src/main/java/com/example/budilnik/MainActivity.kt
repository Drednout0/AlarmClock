package com.arhiser.alarmc

import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    var setAlarm: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        setAlarm = findViewById(R.id.alarm_button)
        setAlarm.setOnClickListener(View.OnClickListener { v: View? ->
            val materialTimePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Выберите время для будильника")
                .build()
            materialTimePicker.addOnPositiveButtonClickListener { view: View? ->
                val calendar = Calendar.getInstance()
                calendar[Calendar.SECOND] = 0
                calendar[Calendar.MILLISECOND] = 0
                calendar[Calendar.MINUTE] = materialTimePicker.minute
                calendar[Calendar.HOUR_OF_DAY] = materialTimePicker.hour
                val alarmManager =
                    getSystemService(ALARM_SERVICE) as AlarmManager
                val alarmClockInfo = AlarmClockInfo(
                    calendar.timeInMillis,
                    alarmInfoPendingIntent
                )
                alarmManager.setAlarmClock(alarmClockInfo, alarmActionPendingIntent)
                Toast.makeText(
                    this,
                    "Будильник установлен на " + sdf.format(calendar.time),
                    Toast.LENGTH_SHORT
                ).show()
            }
            materialTimePicker.show(supportFragmentManager, "tag_picker")
        })
    }

    private val alarmInfoPendingIntent: PendingIntent
        private get() {
            val alarmInfoIntent = Intent(this, MainActivity::class.java)
            alarmInfoIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            return PendingIntent.getActivity(
                this,
                0,
                alarmInfoIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    private val alarmActionPendingIntent: PendingIntent
        private get() {
            val intent = Intent(this, AlarmActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            return PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
}



