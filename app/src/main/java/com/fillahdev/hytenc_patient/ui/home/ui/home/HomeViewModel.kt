package com.fillahdev.hytenc_patient.ui.home.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fillahdev.hytenc_patient.data.MedicineSchedule
import com.fillahdev.hytenc_patient.data.Tips
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel() {

    private val firestore = Firebase.firestore

    val listTips: MutableLiveData<List<Tips>> = MutableLiveData()
    val isTaken: MutableLiveData<String> = MutableLiveData()


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    @SuppressLint("SimpleDateFormat")
    fun getCurrentShcedule(patientName: String) {
        _isLoading.value = true
        val sdf = SimpleDateFormat("HH:mm")
        val currentTime = sdf.parse(sdf.format(Date()))
        firestore.collection("Patient")
            .document(patientName)
            .collection("Medicine Schedule")
            .addSnapshotListener { snapshot, error ->
                _isLoading.value = false
                if (!snapshot?.isEmpty!!) {
                    for (document in snapshot) {
                        val data = document.toObject(MedicineSchedule::class.java)
                        val medicineSchedule = sdf.parse(data.schedule.toString())
                        if (currentTime.toString() == medicineSchedule.toString()
                            &&
                            data.taken.toString() == "false"
                        ) {
                            isTaken.value = data.taken.toString()
                        } else if (currentTime.after(medicineSchedule)
                            &&
                            data.taken.toString() == "false"
                        ) {
                            isTaken.value = data.taken.toString()
                            Log.d("satunich", data.toString())
                        } else if (currentTime.toString() == medicineSchedule.toString()
                            &&
                            data.taken.toString() == "true"
                        ) {
                            isTaken.value = data.taken.toString()
                        } else if (currentTime.after(medicineSchedule)) {
                            isTaken.value = data.taken.toString()
                            Log.d("duanich", data.toString())
                        }
                    }
                } else {
                    isTaken.value = "null"
                }
            }
    }


    fun tellSupervisor(patientName: String) {
        val sdf = SimpleDateFormat("HH:mm")
        val currentTime = sdf.parse(sdf.format(Date()))
        firestore.collection("Patient")
            .document(patientName)
            .collection("Medicine Schedule")
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    for (document in snapshot) {
                        val data = document.toObject(MedicineSchedule::class.java)
                        val medicineSchedule = sdf.parse(data.schedule.toString())
                        if (currentTime.toString() == medicineSchedule.toString()
                            &&
                            data.taken.toString() == "false"
                        ) {
                            firestore.collection("Patient")
                                .document(patientName)
                                .collection("Medicine Schedule")
                                .document(data.medicineName.toString())
                                .update("taken", "true")
                        } else if (data.taken.toString() == "false"
                            &&
                            currentTime.after(medicineSchedule)
                        ) {
                            firestore.collection("Patient")
                                .document(patientName)
                                .collection("Medicine Schedule")
                                .document(data.medicineName.toString())
                                .update("taken", "true")
                        }
                    }
                }
            }

        firestore.collection("NotifyToSupervisor")
            .document(patientName)
            .collection("Has Taken")
            .document(patientName)
            .get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val hasTaken = snapshot.data?.get("hasTaken")
                    if (hasTaken.toString() == "false") {
                        firestore
                            .collection("NotifyToSupervisor")
                            .document(patientName)
                            .collection("Has Taken")
                            .document(patientName)
                            .update("hasTaken", "true")
                    } else {
                        firestore
                            .collection("NotifyToSupervisor")
                            .document(patientName)
                            .collection("Has Taken")
                            .document(patientName)
                            .update("hasTaken", "false")
                    }
                }
            }
    }


    fun getAllTips() {
        firestore.collection("Tips").addSnapshotListener { snapshot, error ->

            if (!snapshot?.isEmpty!!) {
                val _listTips: MutableList<Tips> = mutableListOf()
                for (document in snapshot) {
                    _listTips.add(document.toObject(Tips::class.java))
                }
                listTips.value = _listTips
            }

        }
    }
}