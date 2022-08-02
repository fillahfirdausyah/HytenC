package com.fillahdev.hytenc_patient.util

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase

fun FirestoreDB(): FirebaseFirestore {
    val db = Firebase.firestore
    val settings = firestoreSettings {
        isPersistenceEnabled = false
    }
    db.firestoreSettings = settings
    return db
}