package com.fillahdev.hytenc_patient.ui.home.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fillahdev.hytenc_patient.adapter.ListMedicineScheduleAdapter
import com.fillahdev.hytenc_patient.data.MedicineSchedule
import com.fillahdev.hytenc_patient.databinding.FragmentScheduleBinding
import com.fillahdev.hytenc_patient.service.AlarmReceiver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!
    private val scheduleViewModel by viewModels<ScheduleViewModel>()

    private lateinit var auth: FirebaseAuth
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth
        alarmReceiver = AlarmReceiver()
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = auth.currentUser
        val patientName = currentUser?.displayName

        scheduleViewModel.getMedicineSchedule(patientName.toString())
        scheduleViewModel.listMedicineSchedule.observe(viewLifecycleOwner) { listMedicineSchedule ->
            setupAdapter(listMedicineSchedule)
//            setupAlarm(listMedicineSchedule)
        }

    }

//    private fun setupAlarm(listMedicineSchedule: List<MedicineSchedule>) {
//        for (schedule in listMedicineSchedule) {
//            alarmReceiver.setMedicineScheduleAlarm(
//                requireContext(),
//                schedule.schedule.toString(),
//                schedule.medicineName.toString()
//            )
//        }
//    }

    private fun setupAdapter(listMedicineSchedule: List<MedicineSchedule>) {
        val adapter = ListMedicineScheduleAdapter(listMedicineSchedule)
        binding.rvListSchedule.layoutManager = LinearLayoutManager(requireContext())
        binding.rvListSchedule.setHasFixedSize(true)
        binding.rvListSchedule.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}