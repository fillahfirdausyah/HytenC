package com.fillahdev.hytenc_patient.ui.home.ui.settings

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.fillahdev.hytenc_patient.R
import com.fillahdev.hytenc_patient.data.MedicineSchedule
import com.fillahdev.hytenc_patient.service.AlarmReceiver
import com.fillahdev.hytenc_patient.ui.home.ui.schedule.ScheduleViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SettingsPreference : PreferenceFragmentCompat() {

    private lateinit var alarmReceiver: AlarmReceiver
    private val scheduleViewModel by viewModels<ScheduleViewModel>()
    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preference, rootKey)

        val notificationPreference =
            findPreference<SwitchPreference>(getString(R.string.activate_notification))

        val currentUser = auth.currentUser
        val patientName = currentUser?.displayName

        notificationPreference?.setOnPreferenceChangeListener { preference, newValue ->
            alarmReceiver = AlarmReceiver()
            newValue?.let {
                when (it as Boolean) {
                    true -> {
                        firestore.collection("Patient")
                            .document(patientName.toString())
                            .collection("Medicine Schedule")
                            .get().addOnSuccessListener { snapshot ->
                                if (!snapshot.isEmpty) {
                                    for (document in snapshot) {
                                        val data = document.toObject(MedicineSchedule::class.java)
                                        alarmReceiver.setMedicineScheduleAlarm(
                                            requireContext(),
                                            data.schedule.toString(),
                                            data.medicineName.toString(),
                                            data.id!!
                                        )
                                    }
                                }
                            }
                    }
                    false -> {
                        firestore.collection("Patient")
                            .document(patientName.toString())
                            .collection("Medicine Schedule")
                            .get().addOnSuccessListener { snapshot ->
                                if (!snapshot.isEmpty) {
                                    for (document in snapshot) {
                                        val data = document.toObject(MedicineSchedule::class.java)
                                        alarmReceiver.cancelMedicineScheduleAlarm(
                                            requireContext(),
                                            data.id!!
                                        )
                                    }
                                }
                            }
                    }
                }
            }
            true
        }

    }
}