package com.fillahdev.hytenc_patient.service

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.fillahdev.hytenc_patient.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class AlarmReceiver : BroadcastReceiver() {

    private val firestore = Firebase.firestore
    private val patientName = Firebase.auth.currentUser?.displayName

    override fun onReceive(context: Context, intent: Intent) {

        val medicineName = intent.getStringExtra(EXTRA_MEDICINE_NAME)
        val notifId = intent.getIntExtra(EXTRA_NOTIF_ID, 0)

        Log.d("MEDICINE NAME", medicineName.toString())

        if (medicineName != null) {
            showAlarmNotification(context, notifId)
            Executors.newSingleThreadScheduledExecutor().schedule({
                notifySupervisor()
                updateMedicineData(medicineName)
            }, 30, TimeUnit.SECONDS)
        }
    }

    private fun updateMedicineData(medicineName: String) {
        firestore.collection("Patient")
            .document(patientName.toString())
            .collection("Medicine Schedule")
            .document(medicineName).update("isTaken", "false")
    }

    private fun notifySupervisor() {
        firestore.collection("NotifyToSupervisor")
            .document(patientName.toString())
            .collection("Not Taken")
            .document(patientName.toString())
            .get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    if (snapshot.getString("notTaken").toString() == "true") {
                        firestore.collection("NotifyToSupervisor")
                            .document(patientName.toString())
                            .collection("Not Taken")
                            .document(patientName.toString())
                            .update("notTaken", "false")
                    } else {
                        firestore.collection("NotifyToSupervisor")
                            .document(patientName.toString())
                            .collection("Not Taken")
                            .document(patientName.toString())
                            .update("notTaken", "true")
                    }
                }
            }
    }

    @SuppressLint("InlinedApi")
    fun setMedicineScheduleAlarm(
        context: Context,
        time: String,
        medicineName: String,
        reqCode: Int
    ) {
        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MEDICINE_NAME, medicineName)
        intent.putExtra(EXTRA_NOTIF_ID, reqCode)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                reqCode,
                intent,
                PendingIntent.FLAG_MUTABLE
            )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Log.d("STATUS", "ALARM SET")
        Log.d("MEDICINE NAME", medicineName.toString())
    }

    @SuppressLint("InlinedApi")
    fun cancelMedicineScheduleAlarm(context: Context, reqCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent =
            PendingIntent.getBroadcast(context, reqCode, intent, PendingIntent.FLAG_MUTABLE)

        alarmManager.cancel(pendingIntent)

        Log.d("STATUS", "ALARM CANCELED")
    }

    private fun showAlarmNotification(
        context: Context,
        notifId: Int
    ) {
        val channelId = "Channel_1"
        val channelName = "AlarmManagerChannel"

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification_secondary_solid)
            .setContentTitle("Saatnya Minum Obat")
            .setContentText("Wah sudah waktunya minum obat!")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Sudah waktunya minum obat nih, jangan sampai lupa ya!")
            )
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            channel.setSound(alarmSound, audioAttributes)

            builder.setChannelId(channelId)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManager.notify(notifId, notification)
    }


    companion object {
        const val EXTRA_MEDICINE_NAME = "extra_medicineName"
        const val EXTRA_NOTIF_ID = "extra_notif_id"
    }
}