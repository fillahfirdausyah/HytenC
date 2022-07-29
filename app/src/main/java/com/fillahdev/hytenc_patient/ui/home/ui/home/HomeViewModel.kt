package com.fillahdev.hytenc_patient.ui.home.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fillahdev.hytenc_patient.data.Tips
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class HomeViewModel : ViewModel() {

    private val firestore = Firebase.firestore


    val listTips: MutableLiveData<List<Tips>> = MutableLiveData()


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