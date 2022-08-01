package com.fillahdev.hytenc_patient.ui.home.ui.tips

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fillahdev.hytenc_patient.data.MedicalRecord
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TipsViewModel : ViewModel() {

    private val firestore = Firebase.firestore

    val listMedicalRecord: MutableLiveData<List<MedicalRecord>> = MutableLiveData()


    fun getAllMedicalRecord(patientName: String) {
        firestore.collection("Patient")
            .document(patientName)
            .collection("Medical Record")
            .addSnapshotListener { snapshot, error ->
                if (!snapshot?.isEmpty!!) {
                    val _listMedicalRecord: MutableList<MedicalRecord> = mutableListOf()
                    for (document in snapshot) {
                        _listMedicalRecord.add(document.toObject(MedicalRecord::class.java))
                    }
                    listMedicalRecord.value = _listMedicalRecord
                }
            }
    }

}