package com.fillahdev.hytenc_patient.ui.home.ui.schedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fillahdev.hytenc_patient.data.MedicineSchedule
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ScheduleViewModel : ViewModel() {

    private val firestore = Firebase.firestore

    val listMedicineSchedule: MutableLiveData<List<MedicineSchedule>> = MutableLiveData()


    fun getMedicineSchedule(patientName: String) {
        firestore.collection("Patient")
            .document(patientName)
            .collection("Medicine Schedule").addSnapshotListener { snapshot, error ->
                if (!snapshot?.isEmpty!!) {
                    val _listMedicineSchedule: MutableList<MedicineSchedule> = mutableListOf()
                    for (document in snapshot) {
                        _listMedicineSchedule.add(document.toObject(MedicineSchedule::class.java))
                    }
                    listMedicineSchedule.value = _listMedicineSchedule
                }
            }
    }
}