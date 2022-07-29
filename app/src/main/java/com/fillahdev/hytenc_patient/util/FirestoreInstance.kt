package com.fillahdev.hytenc_patient.util

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase

fun FirestoreInstance(): FirebaseFirestore {
    val firestore = Firebase.firestore
    val settings = firestoreSettings {
        isPersistenceEnabled = true
        cacheSizeBytes = 20000000
        build()
    }
    firestore.firestoreSettings = settings
    return firestore
}