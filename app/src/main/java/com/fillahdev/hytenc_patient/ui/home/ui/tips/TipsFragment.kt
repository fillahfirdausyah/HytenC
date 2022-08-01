package com.fillahdev.hytenc_patient.ui.home.ui.tips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fillahdev.hytenc_patient.adapter.ListMedicalRecordAdapter
import com.fillahdev.hytenc_patient.data.MedicalRecord
import com.fillahdev.hytenc_patient.databinding.FragmentTipsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class TipsFragment : Fragment() {

    private var _binding: FragmentTipsBinding? = null
    private val binding get() = _binding!!
    private val tipsViewModel by viewModels<TipsViewModel>()

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        _binding = FragmentTipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = auth.currentUser
        val patientName = currentUser?.displayName

        tipsViewModel.getAllMedicalRecord(patientName.toString())
        tipsViewModel.listMedicalRecord.observe(viewLifecycleOwner) { listMedicalRecord ->
            setupAdapter(listMedicalRecord)
        }
    }

    private fun setupAdapter(listMedicalRecord: List<MedicalRecord>) {
        val adapter = ListMedicalRecordAdapter(listMedicalRecord)
        binding.rvListMedicalRecord.layoutManager = LinearLayoutManager(requireContext())
        binding.rvListMedicalRecord.setHasFixedSize(true)
        binding.rvListMedicalRecord.adapter = adapter
    }
}