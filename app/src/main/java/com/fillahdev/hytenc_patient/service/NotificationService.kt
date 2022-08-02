package com.fillahdev.hytenc_patient.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        return
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.notification?.let {
            sendNotification(it, message)
        }
    }

    private fun sendNotification(
        remoteMessage: RemoteMessage.Notification,
        message: RemoteMessage
    ) {

    }

}